package net.cjsah.bot.commandV2.source;

import net.cjsah.bot.permission.RoleType;
import org.slf4j.event.Level;

public class ConsoleCommandSource extends CommandSource<CommandSourceContent> {
    public ConsoleCommandSource() {
        super(null);
    }

    @Override
    public boolean hasPermission(RoleType role) {
        return true;
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
