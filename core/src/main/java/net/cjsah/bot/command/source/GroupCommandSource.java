package net.cjsah.bot.command.source;

import net.cjsah.bot.data.GroupSourceData;
import net.cjsah.bot.msg.MessageChain;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.permission.RoleType;
import org.slf4j.event.Level;

public class GroupCommandSource extends CommandSource<GroupSourceData> {

    public GroupCommandSource(GroupSourceData sender) {
        super(sender);
    }

    @Override
    public boolean hasPermission(RoleType role) {
        return PermissionManager.hasPermission("main", this.sender.group().getGroupId(), this.sender.user().getUserId(), role);
    }

    @Override
    public void sendFeedback(MessageChain chain) {

    }

    @Override
    public void sendFeedback(String message) {
        this.sendFeedback(MessageChain.raw(message));
    }

    @Override
    public void sendFeedback(String message, Level level) {
        log.warn("群组消息不应该使用日志形式发送");
        this.sendFeedback(message);
    }
}
