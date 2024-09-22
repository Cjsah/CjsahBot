package net.cjsah.bot.commandV2;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class CommandManager {


    public static void register(Class<?> commandClass) {
        List<Method> methods = Arrays.stream(commandClass.getDeclaredMethods())
                .filter(it -> Modifier.isPublic(it.getModifiers()) && Modifier.isStatic(it.getModifiers()) && !it.isBridge() && it.isAnnotationPresent(Command.class))
                .toList();

        for (Method method : methods) {
            Command annotation = method.getAnnotation(Command.class);
            String cmd = annotation.value();
            CommandReader reader = new CommandReader(cmd);
            reader.parse();


            Parameter[] parameters = method.getParameters();



            for (Parameter parameter : parameters) {
                System.out.println(parameter.getName());
            }

        }
    }
}
