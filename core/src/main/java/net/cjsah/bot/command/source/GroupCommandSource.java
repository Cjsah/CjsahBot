package net.cjsah.bot.command.source;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.exception.CommandException;
import org.slf4j.event.Level;

@Slf4j
public class GroupCommandSource extends CommandSource<Void> {

    public GroupCommandSource() {
        super(null);
    }

    @Override
    public void sendFeedback(String message) throws CommandException {

    }

    @Override
    public void sendFeedback(String message, Level level) throws CommandException {
        log.warn("群组消息不应该使用日志形式发送");
        this.sendFeedback(message);
    }
}
