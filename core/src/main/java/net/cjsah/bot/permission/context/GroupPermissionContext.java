package net.cjsah.bot.permission.context;

import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.config.permission.OverrideRoleUser;
import net.cjsah.bot.config.permission.OverrideUserInGroup;
import net.cjsah.bot.config.permission.PermissionGlobal;
import net.cjsah.bot.config.permission.PermissionPlugin;
import net.cjsah.bot.config.permission.RoledUser;
import net.cjsah.bot.config.permission.UserRole;

import java.util.Collection;
import java.util.Optional;

public class GroupPermissionContext extends PermissionContext {
    private final Permissions permissions;
    private final long userId;
    private final long groupId;
    private final int level;
    private final Enabled enabled;

    public GroupPermissionContext(Permissions permissions, long userId, long groupId) {
        this.permissions = permissions;
        this.userId = userId;
        this.groupId = groupId;

        PermissionGlobal global = permissions.global();
        UserRole role = UserRole.USER;
        Enabled enabled = global.getGroup(groupId)
            .map(it -> Enabled.from(it.enabled()))
            .orElse(Enabled.UNSET);

        if (enabled.enabled()) {
            Optional<RoledUser> optional = global.getUser(userId);
            if (optional.isPresent()) {
                RoledUser user = optional.get();
                role = user.role();
                enabled = Enabled.from(user.enabled());
            }
        }

        Optional<OverrideUserInGroup> optional = global.getUserInGroup(userId, groupId);
        if (optional.isPresent()) {
            OverrideUserInGroup uig = optional.get();
            role = uig.role().orElse(role);
            enabled = uig.enabled().map(Enabled::from).orElse(enabled);
        }

        this.level = role.getLevel();
        this.enabled = enabled;
    }

    @Override
    public boolean hasPermission(UserRole role) {
        return this.enabled.enabled() && this.level >= role.getLevel();
    }

    @Override
    public boolean hasPermission(UserRole role, Collection<String> pluginIds) {
        for (String pluginId : pluginIds) {
            PermissionPlugin plugin = this.permissions.plugins().get(pluginId);
            int level = this.level;
            Enabled enabled = this.enabled;
            if (plugin != null) {

                enabled = plugin.getGroup(this.groupId)
                    .map(it -> Enabled.from(it.enabled()))
                    .orElse(enabled);

                if (enabled.enabled()) {
                    Optional<OverrideRoleUser> userOptional = plugin.getUser(this.userId);
                    if (userOptional.isPresent()) {
                        OverrideRoleUser user = userOptional.get();
                        level = user.role().map(UserRole::getLevel).orElse(level);
                        enabled = user.enabled().map(Enabled::from).orElse(enabled);
                    }

                    Optional<OverrideUserInGroup> uigOptional = plugin.getUserInGroup(this.userId, this.groupId);
                    if (uigOptional.isPresent()) {
                        OverrideUserInGroup uig = uigOptional.get();
                        level = uig.role().map(UserRole::getLevel).orElse(level);
                        enabled = uig.enabled().map(Enabled::from).orElse(enabled);
                    }
                }

                if (enabled == Enabled.UNSET) {
                    enabled = Enabled.from(plugin.defaultEnabled());
                }
            }
            if (enabled.enabled() && level >= role.getLevel()) {
                return true;
            }
        }
        return pluginIds.isEmpty() && this.hasPermission(role);
    }

    @Override
    public boolean hasPermission(String pluginId) {
        PermissionPlugin plugin = this.permissions.plugins().get(pluginId);
        Enabled enabled = this.enabled;
        if (plugin != null) {

            enabled = plugin.getGroup(this.groupId)
                .map(it -> Enabled.from(it.enabled()))
                .orElse(enabled);

            if (enabled.enabled()) {
                enabled = plugin.getUser(this.userId)
                    .flatMap(OverrideRoleUser::enabled)
                    .map(Enabled::from)
                    .orElse(enabled);

                enabled = plugin.getUserInGroup(this.userId, this.groupId)
                    .flatMap(OverrideUserInGroup::enabled)
                    .map(Enabled::from)
                    .orElse(enabled);
            }

            if (enabled == Enabled.UNSET) {
                enabled = Enabled.from(plugin.defaultEnabled());
            }
        }
        return enabled.enabled();
    }
}
