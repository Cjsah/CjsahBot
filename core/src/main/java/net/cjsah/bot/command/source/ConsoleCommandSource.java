package net.cjsah.bot.command.source;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

@Slf4j
public class ConsoleCommandSource extends CommandSource<Void> {
    public ConsoleCommandSource() {
        super(null);
    }

    @Override
    public void sendFeedback(String message) {
        this.sendFeedback(message, Level.INFO);
    }

    @Override
    public void sendFeedback(String message, Level level) {
        switch (level) {
            case INFO -> log.info(message);
            case WARN -> log.warn(message);
            case ERROR -> log.error(message);
            case DEBUG -> log.debug(message);
        }
    }
}