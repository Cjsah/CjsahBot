package net.cjsah.bot.permission.context;

import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.config.permission.OverrideRoleUser;
import net.cjsah.bot.config.permission.OverrideUserInGroup;
import net.cjsah.bot.config.permission.PermissionGlobal;
import net.cjsah.bot.config.permission.PermissionPlugin;
import net.cjsah.bot.config.permission.RoledGroup;
import net.cjsah.bot.config.permission.RoledUser;
import net.cjsah.bot.permission.PermissionRole;
import org.jetbrains.annotations.Nullable;

public abstract class PermissionContext {
    protected final Permissions permissions;
    protected final String pluginId;
    protected final long userId;
    @Nullable
    protected final Long groupId;
    protected int level = PermissionRole.USER.getLevel();
    protected boolean enabled = true;

    protected PermissionContext(Permissions permissions, String pluginId, long userId, @Nullable Long groupId) {
        this.permissions = permissions;
        this.pluginId = pluginId;
        this.userId = userId;
        this.groupId = groupId;
    }

    protected void resolveGlobalUser() {
        PermissionGlobal global = this.permissions.global();
        for (RoledUser user : global.users()) {
            if (user.id() == this.userId) {
                this.level = user.role().getRole();
                this.enabled = user.enabled();
                return;
            }
        }
    }

    protected void resolvePluginUser() {
        PermissionPlugin plugin = this.permissions.plugins().get(this.pluginId);
        if (plugin != null) {
            this.enabled = plugin.defaultEnabled();
            for (OverrideRoleUser user : plugin.users()) {
                if (user.id() == this.userId) {
                    user.role().ifPresent(r -> this.level = r.getRole());
                    user.enabled().ifPresent(e -> this.enabled = e);
                    return;
                }
            }
        }
    }

    protected void resolveGlobalGroup() {
        if (this.groupId == null) return;
        PermissionGlobal global = this.permissions.global();
        for (RoledGroup group : global.groups()) {
            if (group.id() == this.groupId) {
                this.enabled = group.enabled();
                break;
            }
        }
        for (OverrideUserInGroup uig : global.userInGroups()) {
            if (uig.userId() == this.userId && uig.groupId() == this.groupId) {
                uig.role().ifPresent(r -> this.level = r.getRole());
                uig.enabled().ifPresent(e -> this.enabled = e);
                return;
            }
        }
    }

    protected void resolvePluginGroup() {
        if (this.groupId == null) return;
        PermissionPlugin plugin = this.permissions.plugins().get(this.pluginId);
        if (plugin != null) {
            for (RoledGroup group : plugin.groups()) {
                if (group.id() == this.groupId) {
                    this.enabled = group.enabled();
                    break;
                }
            }
            for (OverrideUserInGroup uig : plugin.userInGroups()) {
                if (uig.userId() == this.userId && uig.groupId() == this.groupId) {
                    uig.role().ifPresent(r -> this.level = r.getRole());
                    uig.enabled().ifPresent(e -> this.enabled = e);
                    return;
                }
            }
        }
    }

    public long getUserId() {
        return this.userId;
    }

    @Nullable
    public Long getGroupId() {
        return this.groupId;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean hasPermission(PermissionRole required) {
        return this.enabled && this.level >= required.getLevel();
    }
}
