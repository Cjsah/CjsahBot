package net.cjsah.bot.commandV2;

import net.cjsah.bot.commandV2.context.CommandParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public static final Logger log = LoggerFactory.getLogger(CommandManager.class);

    public static void register(Class<?> commandClass) {
        List<Method> methods = Arrays.stream(commandClass.getDeclaredMethods())
                .filter(it -> Modifier.isPublic(it.getModifiers()) && Modifier.isStatic(it.getModifiers()) && !it.isBridge() && it.isAnnotationPresent(Command.class))
                .toList();

        for (Method method : methods) {
            Command annotation = method.getAnnotation(Command.class);
            String cmd = annotation.value();

            List<? extends Class<?>> params = Arrays.stream(method.getParameters()).map(Parameter::getType).toList();


            CommandParser reader = new CommandParser(cmd);
            reader.parse(params);


            Parameter[] parameters = method.getParameters();



            for (Parameter parameter : parameters) {
                System.out.println(parameter.getName());
                System.out.println(parameter.getType());
                System.out.println("===");
            }

        }
    }
}
