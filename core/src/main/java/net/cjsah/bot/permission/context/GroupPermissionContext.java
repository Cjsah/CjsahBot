package net.cjsah.bot.permission.context;

import net.cjsah.bot.config.Permissions;

public class GroupPermissionContext extends PermissionContext {

    public GroupPermissionContext(Permissions permissions, String pluginId, long userId, long groupId) {
        super(permissions, pluginId, userId, groupId);
        this.resolveGlobalUser();
        this.resolveGlobalGroup();
        this.resolvePluginUser();
        this.resolvePluginGroup();
    }
}
