package net.cjsah.bot.permission.context;

import net.cjsah.bot.config.Permissions;

public class FriendPermissionContext extends PermissionContext {

    public FriendPermissionContext(Permissions permissions, String pluginId, long userId) {
        super(permissions, pluginId, userId, null);
        this.resolveGlobalUser();
        this.resolvePluginUser();
    }
}
