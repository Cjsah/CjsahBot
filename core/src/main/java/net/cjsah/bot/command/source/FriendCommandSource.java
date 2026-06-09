package net.cjsah.bot.command.source;

import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.event.events.FriendMessageEvent;
import net.cjsah.bot.permission.context.FriendPermissionContext;
import net.cjsah.bot.permission.context.PermissionContext;

public final class FriendCommandSource extends CommandSource<FriendMessageEvent> {

    public FriendCommandSource(FriendMessageEvent sender) {
        super(sender);
    }

    @Override
    public PermissionContext createPermissionContext(Permissions permissions, String pluginId) {
        return new FriendPermissionContext(permissions, pluginId, this.sender.getUserId());
    }

    @Override
    public void sendFeedback(String message) {
    }
}
