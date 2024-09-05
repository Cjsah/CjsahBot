package net.cjsah.bot.command.source;

import net.cjsah.bot.api.Api;
import net.cjsah.bot.data.UserBaseData;
import net.cjsah.bot.msg.MessageChain;
import org.slf4j.event.Level;

public class UserCommandSource extends CommandSource<UserBaseData> {
    public UserCommandSource(UserBaseData user) {
        super(user);
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
        log.warn("好友消息不应该使用日志形式发送");
        this.sendFeedback(message);
    }
}
