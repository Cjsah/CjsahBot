package net.cjsah.bot.command.source;

import net.cjsah.bot.MainApplication;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.permission.context.ConsolePermissionContext;
import net.cjsah.bot.permission.context.PermissionContext;

import java.util.function.Function;

public final class ConsoleCommandSource extends CommandSource<ConsoleCommandSource.ConsoleCommandSender> {

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
            MainApplication.log.info(line);
        }
    }

    @Override
    public void sendFeedback(MessageChain message) {
        this.sendFeedback(message.toString());
    }

    static class ConsoleCommandSender implements CommandSender {

        @Override
        public long getUserId() {
            return 0;
        }
    }

}
