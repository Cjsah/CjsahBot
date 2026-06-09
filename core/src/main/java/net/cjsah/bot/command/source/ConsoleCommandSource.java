package net.cjsah.bot.command.source;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.permission.context.ConsolePermissionContext;
import net.cjsah.bot.permission.context.PermissionContext;

import java.util.function.Function;

@Slf4j(topic = "Command")
public final class ConsoleCommandSource extends CommandSource<Void> {

    public ConsoleCommandSource() {
        super(null);
    }

    @Override
    protected Function<Permissions, PermissionContext> permissionFactory() {
        return permissions -> new ConsolePermissionContext();
    }

    @Override
    public void sendFeedback(String message) {
        for (String line : message.split("\n")) {
            log.info(line);
        }
    }
}
