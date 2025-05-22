package net.cjsah.bot.command.source;

import net.cjsah.bot.api.TypedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConsoleCommandSource extends CommandSource<Void> {

    private static final Logger log = LoggerFactory.getLogger(ConsoleCommandSource.class);

    public ConsoleCommandSource() {
        super(null);
    }

    @Override
    public long getSenderId() {
        return "CommandConsole".hashCode();
    }

    @Override
    public void sendFeedback(TypedMessage message) {
        log.info("{}", message.getContent());
    }
}
