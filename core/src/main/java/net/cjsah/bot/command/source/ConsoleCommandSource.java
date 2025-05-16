package net.cjsah.bot.command.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConsoleCommandSource extends CommandSource<Void> {

    private static final Logger log = LoggerFactory.getLogger(ConsoleCommandSource.class);

    public ConsoleCommandSource() {
        super(null);
    }

    @Override
    public void sendFeedback(String message) {
        log.info(message);
    }
}
