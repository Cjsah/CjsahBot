package net.cjsah.bot.command.source;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Command")
public final class ConsoleCommandSource extends CommandSource<Void> {

    public ConsoleCommandSource() {
        super(null);
    }

    @Override
    public void sendFeedback(String message) {
        for (String line : message.split("\n")) {
            log.info(line);
        }
    }
}
