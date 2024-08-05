package net.cjsah.bot.command.source;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.api.Api;
import net.cjsah.bot.data.GroupSourceData;
import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.msg.MessageChain;
import org.slf4j.event.Level;

@Slf4j
public class GroupCommandSource extends CommandSource<GroupSourceData> {

    public GroupCommandSource() {
        super(null);
    }

    @Override
    public void sendFeedback(String message) throws CommandException {
        Api.sendGroupMsg(this.sender.getGroup().getGroupId(), MessageChain.raw(message));
    }

    @Override
    public void sendFeedback(String message, Level level) throws CommandException {
        log.warn("群组消息不应该使用日志形式发送");
        this.sendFeedback(message);
    }
}
