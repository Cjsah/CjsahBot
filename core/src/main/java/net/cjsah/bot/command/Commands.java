package net.cjsah.bot.command;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.plugin.PluginContext;
import net.cjsah.bot.plugin.PluginInfo;

import java.util.function.Predicate;

@Slf4j(topic = "CommandManager")
public class Commands {
    private static final CommandDispatcher dispatcher = new CommandDispatcher();

    public static CommandRegisterContext registerContext() {
        PluginInfo info = PluginContext.getCurrentPluginInfo();
        return new CommandRegisterContext(dispatcher, info);
    }

    public static void deregisterPlugin(String pluginId) {
        dispatcher.getRoot().removePlugin(pluginId);
    }

    public static void execute(CommandSource<?> source, String cmd) {
        try {
            dispatcher.execute(cmd, source);
        } catch (CommandException e) {
            log.error("Failed to execute command", e);
        }
    }

    public static <S> Predicate<S> passRequirement() {
        return c -> true;
    }

}
