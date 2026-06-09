package net.cjsah.bot.permission.context;

import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.config.permission.OverrideRoleUser;
import net.cjsah.bot.config.permission.PermissionGlobal;
import net.cjsah.bot.config.permission.PermissionPlugin;
import net.cjsah.bot.config.permission.RoledUser;
import net.cjsah.bot.config.permission.UserRole;

import java.util.Collection;
import java.util.Optional;

public class FriendPermissionContext extends PermissionContext {
    private final Permissions permissions;
    private final long userId;
    private final int level;
    private final Enabled enabled;

    public FriendPermissionContext(Permissions permissions, long userId) {
        this.permissions = permissions;
        this.userId = userId;
        PermissionGlobal global = permissions.global();
        UserRole role = UserRole.USER;
        Enabled enabled = Enabled.UNSET;
        Optional<RoledUser> optional = global.getUser(userId);
        if (optional.isPresent()) {
            RoledUser user = optional.get();
            role = user.role();
            enabled = Enabled.from(user.enabled());
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
                Optional<OverrideRoleUser> optional = plugin.getUser(this.userId);
                if (optional.isPresent()) {
                    OverrideRoleUser user = optional.get();
                    level = user.role().map(UserRole::getLevel).orElse(level);
                    enabled = user.enabled().map(Enabled::from).orElse(enabled);
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
}
