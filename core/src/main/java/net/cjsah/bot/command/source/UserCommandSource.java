package net.cjsah.bot.command.source;

import net.cjsah.bot.api.Api;
import net.cjsah.bot.event.events.MessageEvent;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.permission.RoleType;
import org.slf4j.event.Level;

public class UserCommandSource extends CommandSource<MessageEvent> {
    public UserCommandSource(MessageEvent msg) {
        super(msg);
    }

    @Override
    public boolean hasPermission(RoleType role) {
        return PermissionManager.hasPermission("main", 0, this.sender.getUserId(), role);
    }

    @Override
    public void sendFeedback(String message) {
        Api.sendMsg(message, this.sender.getRoomId(), this.sender.getChannelId());
    }

    @Override
    public void sendFeedback(String message, Level level) {
        log.warn("此消息不应该使用日志形式发送");
        this.sendFeedback(message);
    }
}
