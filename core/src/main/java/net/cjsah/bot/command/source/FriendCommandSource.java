package net.cjsah.bot.command.source;

import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.event.events.FriendMessageEvent;
import net.cjsah.bot.packet.request.payload.SendFriendMsg;
import net.cjsah.bot.permission.context.FriendPermissionContext;
import net.cjsah.bot.permission.context.PermissionContext;

import java.util.function.Function;

public final class FriendCommandSource extends CommandSource<FriendMessageEvent> {

    public FriendCommandSource(FriendMessageEvent sender) {
        super(sender);
    }

    @Override
    protected Function<Permissions, PermissionContext> permissionFactory() {
        return permissions -> new FriendPermissionContext(permissions, this.sender.getUserId());
    }

    @Override
    public void sendFeedback(String message) {
        this.sendFeedback(new SendFriendMsg(this.sender.getUserId(), message));
    }

    @Override
    public void sendFeedback(MessageChain message) {
        this.sendFeedback(new SendFriendMsg(this.sender.getUserId(), message));
    }
}
