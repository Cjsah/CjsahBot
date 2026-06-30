package net.cjsah.bot.resolver.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MultilinePatternLayout extends PatternLayout {

    @Override
    public String doLayout(ILoggingEvent event) {
        String[] part = super.doLayout(event).split("\\$split\\$");
        String prefix = part[0];

        String[] messages = part[1].replaceAll("^\r+|\r+$", "").split("\n");
        return Arrays.stream(messages).parallel()
            .map(it -> prefix + it.replaceAll("^\r+|\r+$", "") + CoreConstants.LINE_SEPARATOR)
            .collect(Collectors.joining());
    }

}
