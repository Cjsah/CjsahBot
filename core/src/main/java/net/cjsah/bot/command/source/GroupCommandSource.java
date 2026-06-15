package net.cjsah.bot.command.source;

import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.event.events.GroupMessageEvent;
import net.cjsah.bot.packet.request.payload.SendGroupMsg;
import net.cjsah.bot.permission.context.GroupPermissionContext;
import net.cjsah.bot.permission.context.PermissionContext;

import java.util.function.Function;

public final class GroupCommandSource extends CommandSource<GroupMessageEvent> {

    public GroupCommandSource(GroupMessageEvent sender) {
        super(sender);
    }

    @Override
    protected Function<Permissions, PermissionContext> permissionFactory() {
        return permissions -> new GroupPermissionContext(permissions, this.sender.getUserId(), this.sender.getGroupId());
    }

    @Override
    public void sendFeedback(String message) {
        this.sendFeedback(new SendGroupMsg(this.sender.getGroupId(), message));
    }

    @Override
    public void sendFeedback(MessageChain message) {
        this.sendFeedback(new SendGroupMsg(this.sender.getGroupId(), message));
    }
}
