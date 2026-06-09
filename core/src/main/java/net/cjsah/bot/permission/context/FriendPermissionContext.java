package net.cjsah.bot.permission.context;

import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.config.permission.OverrideRoleUser;
import net.cjsah.bot.config.permission.PermissionGlobal;
import net.cjsah.bot.config.permission.PermissionPlugin;
import net.cjsah.bot.config.permission.RoledUser;
import net.cjsah.bot.config.permission.UserRole;
import net.cjsah.bot.permission.PermissionRole;

import java.util.Collection;

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
        for (RoledUser user : global.users()) {
            if (user.id() == userId) {
                role = user.role();
                enabled = Enabled.from(user.enabled());
                break;
            }
        }
        this.level = role.getLevel();
        this.enabled = enabled;
    }

    @Override
    public boolean hasPermission(PermissionRole role) {
        return this.enabled.enabled() && this.level >= role.getLevel();
    }

    @Override
    public boolean hasPermission(PermissionRole role, Collection<String> pluginIds) {
        for (String pluginId : pluginIds) {
            PermissionPlugin plugin = this.permissions.plugins().get(pluginId);
            int level = this.level;
            Enabled enabled = this.enabled;
            if (plugin != null) {
                for (OverrideRoleUser user : plugin.users()) {
                    if (user.id() == this.userId) {
                        level = user.role().map(UserRole::getLevel).orElse(level);
                        enabled = user.enabled().map(Enabled::from).orElse(enabled);
                        break;
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
        return pluginIds.isEmpty();
    }
}
