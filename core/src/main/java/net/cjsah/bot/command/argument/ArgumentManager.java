package net.cjsah.bot.command.argument;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.cjsah.bot.command.argument.more.AtArgument;
import net.cjsah.bot.exception.BuiltinExceptions;
import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.plugin.builtin.CorePlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ArgumentManager {
    private static final Table<String, String, Function<String, Argument<?>>> arguments = HashBasedTable.create();
    private static final Map<Class<?>, Class<?>> PRIMITIVE_CLASS = new HashMap<>();


    static {
        String id = CorePlugin.INSTANCE.id();
        register(id, _ -> BooleanArgument.bool(), "Boolean", "boolean", "bool");
        register(id, ByteArgument::byteArg, "Byte", "byte");
        register(id, ShortArgument::shortArg, "Short", "short");
        register(id, IntArgument::intArg, "Integer", "integer", "int");
        register(id, LongArgument::longArg, "Long", "long");
        register(id, FloatArgument::floatArg, "Float", "float");
        register(id, DoubleArgument::doubleArg, "Double", "double");
        register(id, StringArgument::byArg, "String", "string", "str");
        register(id, _ -> AtArgument.atArg(), "At", "at");

        PRIMITIVE_CLASS.put(boolean.class, Boolean.class);
        PRIMITIVE_CLASS.put(byte.class, Byte.class);
        PRIMITIVE_CLASS.put(short.class, Short.class);
        PRIMITIVE_CLASS.put(char.class, Character.class);
        PRIMITIVE_CLASS.put(int.class, Integer.class);
        PRIMITIVE_CLASS.put(long.class, Long.class);
        PRIMITIVE_CLASS.put(float.class, Float.class);
        PRIMITIVE_CLASS.put(double.class, Double.class);
    }

    public static Class<?> getDefaultClass(Class<?> clazz) {
        return PRIMITIVE_CLASS.getOrDefault(clazz, clazz);
    }

    public static void register(String id, Function<String, Argument<?>> factory, String... keys) {
        for (String key : keys) {
            if (arguments.containsColumn(key)) {
                throw new IllegalArgumentException("Type '%s' is already exists".formatted(key));
            }
            arguments.put(id, key, factory);
        }
    }

    public static Argument<?> getArgument(String type, String param) throws CommandException {
        Map<String, Function<String, Argument<?>>> column = arguments.column(type);
        if (column.isEmpty()) {
            throw BuiltinExceptions.PARSE_ARGUMENT_NOT_EXIST.create(type);
        }
        Function<String, Argument<?>> factory = column.values().stream().findFirst().orElseThrow();
        return factory.apply(param);
    }

}
