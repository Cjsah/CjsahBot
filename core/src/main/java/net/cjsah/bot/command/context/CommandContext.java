package net.cjsah.bot.command.context;

import net.cjsah.bot.command.Command;
import net.cjsah.bot.command.source.CommandSource;

import java.util.HashMap;
import java.util.Map;

public final class CommandContext {
    private final Map<String, ParsedNode<?>> arguments;
    private final CommandSource<?> source;
    private final Command command;

    public CommandContext(Map<String, ParsedNode<?>> arguments, CommandSource<?> source, Command command) {
        this.arguments = arguments;
        this.source = source;
        this.command = command;
    }

    @SuppressWarnings("unchecked")
    public <V> V getArgument(String name, Class<V> type) {
        ParsedNode<?> argument = this.arguments.get(name);
        if (argument == null) {
            throw new IllegalArgumentException("Argument '" + name + "' not found");
        }
        Object result = argument.getResult();
        if (ClassConversion.get(type).isAssignableFrom(result.getClass())) {
            return (V) result;
        } else {
            throw new IllegalArgumentException("参数 '" + name + "' 被定义为 " + result.getClass().getSimpleName() + ", 而不是 " + type);
        }
    }

    public CommandSource<?> getSource() {
        return this.source;
    }

    public Command getCommand() {
        return this.command;
    }

    static class ClassConversion {
        private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<>();

        public static Class<?> get(Class<?> clazz) {
            return PRIMITIVE_TO_WRAPPER.getOrDefault(clazz, clazz);
        }

        static {
            PRIMITIVE_TO_WRAPPER.put(boolean.class, Boolean.class);
            PRIMITIVE_TO_WRAPPER.put(byte.class, Byte.class);
            PRIMITIVE_TO_WRAPPER.put(short.class, Short.class);
            PRIMITIVE_TO_WRAPPER.put(char.class, Character.class);
            PRIMITIVE_TO_WRAPPER.put(int.class, Integer.class);
            PRIMITIVE_TO_WRAPPER.put(long.class, Long.class);
            PRIMITIVE_TO_WRAPPER.put(float.class, Float.class);
            PRIMITIVE_TO_WRAPPER.put(double.class, Double.class);
        }
    }
}
