package net.cjsah.bot.permission.context;

import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.config.permission.OverrideRoleUser;
import net.cjsah.bot.config.permission.OverrideUserInGroup;
import net.cjsah.bot.config.permission.PermissionGlobal;
import net.cjsah.bot.config.permission.PermissionPlugin;
import net.cjsah.bot.config.permission.RoledGroup;
import net.cjsah.bot.config.permission.RoledUser;
import net.cjsah.bot.config.permission.UserRole;
import net.cjsah.bot.permission.PermissionRole;

import java.util.Collection;

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
        Enabled enabled = Enabled.UNSET;

        for (RoledUser user : global.users()) {
            if (user.id() == userId) {
                role = user.role();
                enabled = user.enabled() ? Enabled.ENABLED : Enabled.DISABLED;
                break;
            }
        }

        for (RoledGroup group : global.groups()) {
            if (group.id() == groupId) {
                enabled = group.enabled() ? Enabled.ENABLED : Enabled.DISABLED;
                break;
            }
        }

        for (OverrideUserInGroup uig : global.userInGroups()) {
            if (uig.userId() == userId && uig.groupId() == groupId) {
                uig.role().ifPresent(r -> role = r);
                uig.enabled().ifPresent(e -> enabled = e ? Enabled.ENABLED : Enabled.DISABLED);
                break;
            }
        }

        this.level = role.getLevel();
        this.enabled = enabled;
    }

    @Override
    public boolean hasPermission(PermissionRole role) {
        return this.enabled != Enabled.DISABLED && this.level >= role.getLevel();
    }

    @Override
    public boolean hasPermission(PermissionRole role, Collection<String> pluginIds) {
        for (String pluginId : pluginIds) {
            PermissionPlugin plugin = this.permissions.plugins().get(pluginId);
            int resolvedLevel = this.level;
            Enabled enabled;
            if (plugin != null) {
                enabled = plugin.defaultEnabled() ? Enabled.ENABLED : Enabled.DISABLED;

                for (OverrideRoleUser user : plugin.users()) {
                    if (user.id() == this.userId) {
                        user.role().ifPresent(r -> resolvedLevel = r.getLevel());
                        user.enabled().ifPresent(e -> enabled = e ? Enabled.ENABLED : Enabled.DISABLED);
                        break;
                    }
                }

                for (RoledGroup group : plugin.groups()) {
                    if (group.id() == this.groupId) {
                        enabled = group.enabled() ? Enabled.ENABLED : Enabled.DISABLED;
                        break;
                    }
                }

                for (OverrideUserInGroup uig : plugin.userInGroups()) {
                    if (uig.userId() == this.userId && uig.groupId() == this.groupId) {
                        uig.role().ifPresent(r -> resolvedLevel = r.getLevel());
                        uig.enabled().ifPresent(e -> enabled = e ? Enabled.ENABLED : Enabled.DISABLED);
                        break;
                    }
                }
            } else {
                enabled = this.enabled;
            }
            if (enabled != Enabled.DISABLED && resolvedLevel >= role.getLevel()) {
                return true;
            }
        }
        return false;
    }
}
