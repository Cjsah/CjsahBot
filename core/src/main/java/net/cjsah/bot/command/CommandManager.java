package net.cjsah.bot.command;

import net.cjsah.bot.command.argument.special.ArgsArgument;
import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.argument.special.CommandSourceArgument;
import net.cjsah.bot.command.context.CommandNode;
import net.cjsah.bot.command.context.CommandNodeBuilder;
import net.cjsah.bot.command.context.CommandParameter;
import net.cjsah.bot.command.context.CommandParser;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.plugin.PluginContext;
import net.cjsah.bot.plugin.PluginThreadPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private static final Logger log = LoggerFactory.getLogger(CommandManager.class);

    private static final Map<String, CommandNode> COMMANDS = new HashMap<>();

    public static void register(Class<?> commandClass) {
        List<Method> methods = Arrays.stream(commandClass.getDeclaredMethods())
                .filter(it -> Modifier.isPublic(it.getModifiers()) && Modifier.isStatic(it.getModifiers()) && !it.isBridge() && it.isAnnotationPresent(Command.class))
                .toList();

        for (Method method : methods) {
            try {
                Command annotation = method.getAnnotation(Command.class);
                String cmd = annotation.value();
                CommandParser parser = new CommandParser(cmd);
                CommandNodeBuilder builder = parser.parse(method.getParameters());
                builder.setMethod(method);
                builder.setRole(annotation.role());
                builder.setPlugin(PluginContext.getCurrentPluginInfo().getId());
                CommandNode node = builder.build();
                if (COMMANDS.containsKey(node.getName())) {
                    throw BuiltExceptions.REPEAT_COMMAND.create();
                }
                COMMANDS.put(node.getName(), node);
            } catch (CommandException e) {
                log.warn("命令 {} 注册失败: {}", method, e.getMessage());
            }

        }
    }

    public static void deregister(String pluginId) {
        List<String> keys = COMMANDS.entrySet()
                .stream().parallel()
                .filter(it -> it.getValue().getPluginId().equals(pluginId))
                .map(Map.Entry::getKey)
                .toList();
        keys.forEach(COMMANDS::remove);
    }

    public static void execute(String command, Map<String, String> options, CommandSource source) {
        try {
            CommandNode node = COMMANDS.get(command);
            if (node == null) throw BuiltExceptions.DISPATCHER_UNKNOWN_COMMAND.create();
            if (!source.hasPermission(node.getRole())) throw BuiltExceptions.DISPATCHER_COMMAND_NO_PERMISSION.create();
            List<CommandParameter> parameters = node.getParameters();
            Object[] args = new Object[parameters.size()];
            for (int i = 0; i < parameters.size(); i++) {
                CommandParameter parameter = parameters.get(i);
                Class<? extends Argument<?>> resolver = parameter.resolver();
                if (resolver == ArgsArgument.class) {
                    args[i] = options;
                    continue;
                }
                if (resolver == CommandSourceArgument.class) {
                    args[i] = source;
                    continue;
                }
                String value = options.get(parameter.name());
                if (value == null) {
                    args[i] = null;
                    continue;
                }
                Constructor<? extends Argument<?>> constructor = resolver.getDeclaredConstructor();
                Argument<?> argument = constructor.newInstance();
                args[i] = argument.parse(value);
            }
            PluginThreadPools.execute(node.getPluginId(), () -> {
                try {
                    node.getMethod().invoke(null, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("Failed to execute command", e);
                }
            });
        } catch (CommandException e) { // 命令导致的异常
            log.warn("Failed to execute command: {}", e.getMessage());
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) { // 开发者导致的异常
            log.error("Failed to execute command: {}", e.getMessage());
        }

    }
}
