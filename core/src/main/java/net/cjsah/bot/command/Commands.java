package net.cjsah.bot.command;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.BuiltinExceptions;
import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.plugin.PluginManager;
import net.cjsah.bot.plugin.PluginMetadata;

import java.util.function.Predicate;

@Slf4j(topic = "CommandManager")
public class Commands {
    private static final CommandDispatcher dispatcher = new CommandDispatcher();

    public static CommandRegisterContext registerContext() {
        PluginMetadata info = PluginManager.getCurrentInfo();
        if (info == null) {
            throw BuiltinExceptions.NOT_IN_PLUGIN.create();
        }
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
        return _ -> true;
    }

}
