package net.cjsah.bot.command;

import cn.hutool.core.lang.Pair;
import com.google.common.collect.Iterables;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.command.context.ParsedCommandNode;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.exception.BuiltinExceptions;
import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.plugin.PluginManager;
import net.cjsah.bot.plugin.PluginMetadata;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j(topic = "CommandManager", access = AccessLevel.PUBLIC)
public class Commands {
    private static final CommandDispatcher Dispatcher = new CommandDispatcher();

    public static CommandRegisterContext registerContext() {
        PluginMetadata info = PluginManager.getCurrentInfo();
        if (info == null) {
            throw BuiltinExceptions.NOT_IN_PLUGIN.create();
        }
        return new CommandRegisterContext(Dispatcher, info);
    }

    public static void deregisterPlugin(String pluginId) {
        Dispatcher.getRoot().removePlugin(pluginId);
    }

    public static void execute(CommandSource<?> source, String cmd) {
        try {
            Dispatcher.execute(cmd, source);
        } catch (CommandException e) {
            log.error("Failed to execute command", e);
        }
    }

    public static List<Pair<String, String>> getHelp(@Nullable String command, CommandSource<?> source) throws CommandException {
        CommandNode node = parseCommandNode(command, source);
        String prefix = node == Dispatcher.getRoot() ? "" : command + " ";

        Map<CommandNode, String> usages = Dispatcher.getSmartUsage(node, source);

        return usages.entrySet().stream()
            .map(it -> Pair.of("/" + prefix + it.getValue(), it.getKey().getDescription()))
            .toList();
    }

    private static CommandNode parseCommandNode(@Nullable String command, CommandSource<?> source) throws CommandException {
        if (command == null || command.isEmpty()) return Dispatcher.getRoot();

        ParseResults results = Dispatcher.parse(command, source);
        List<ParsedCommandNode> nodes = results.context().getNodes();

        if (nodes.isEmpty()) {
            throw BuiltinExceptions.DISPATCHER_UNKNOWN_COMMAND.create();
        }

        return nodes.getLast().node();
    }


    public static <S> Predicate<S> passRequirement() {
        return _ -> true;
    }

}
