package net.cjsah.bot.command.source;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.api.Api;
import net.cjsah.bot.data.UserBaseData;
import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.msg.MessageChain;
import org.slf4j.event.Level;

@Slf4j
public class UserCommandSource extends CommandSource<UserBaseData> {

    public UserCommandSource(UserBaseData user) {
        super(user);
    }

    @Override
    public void sendFeedback(String message) throws CommandException {
        Api.sendPrivateMsg(this.source.getUserId(), MessageChain.raw(message));
    }

    @Override
    public void sendFeedback(String message, Level level) throws CommandException {
        log.warn("好友消息不应该使用日志形式发送");
        this.sendFeedback(message);
    }
}
