package net.cjsah.bot.commandV2;

import net.cjsah.bot.commandV2.context.CommandNode;
import net.cjsah.bot.commandV2.context.CommandParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    public static final Logger log = LoggerFactory.getLogger(CommandManager.class);

    private static final Map<String, CommandNode> Commands = new HashMap<>();

    public static void register(Class<?> commandClass) {
        List<Method> methods = Arrays.stream(commandClass.getDeclaredMethods())
                .filter(it -> Modifier.isPublic(it.getModifiers()) && Modifier.isStatic(it.getModifiers()) && !it.isBridge() && it.isAnnotationPresent(Command.class))
                .toList();

        for (Method method : methods) {
            Command annotation = method.getAnnotation(Command.class);
            String cmd = annotation.value();

            List<? extends Class<?>> params = Arrays.stream(method.getParameters()).map(Parameter::getType).toList();

            CommandParser parser = new CommandParser(cmd);
            CommandNode node = parser.parse(params);
            Commands.put(node.getName(), node);
        }
    }

    public static void execute(String command) {

    }
}
