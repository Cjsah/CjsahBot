package net.cjsah.bot.resolver;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogLevelColorConverter extends ClassicConverter {
    private static final String INFO = "\033[0;32m";
    private static final String WARN = "\033[0;33m";
    private static final String ERROR = "\033[0;31m";
    private static final String DEBUG = "\033[0;34m";
    private static final String END = "\033[m";

    @Override
    public String convert(ILoggingEvent event) {
        return getColor(event.getLevel()) + event.getLevel() + END;
    }

    private static String getColor(Level level) {
        return switch (level.levelInt) {
            case Level.INFO_INT -> INFO;
            case Level.WARN_INT -> WARN;
            case Level.ERROR_INT -> ERROR;
            case Level.DEBUG_INT -> DEBUG;
            default -> "";
        };
    }
}
