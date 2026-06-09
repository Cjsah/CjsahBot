package net.cjsah.bot.command.argument;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.plugin.MainPlugin;

import java.util.Map;
import java.util.function.Function;

public class ArgumentManager {
    private static final Table<String, String, Function<String, Argument<?>>> arguments;

    static {
        arguments = HashBasedTable.create();
        String id = MainPlugin.PLUGIN_INFO.getId();
        register(id, arg -> BooleanArgument.bool(), "Boolean", "boolean", "bool");
        register(id, ByteArgument::byteArg, "Byte", "byte");
        register(id, ShortArgument::shortArg, "Short", "short");
        register(id, IntArgument::intArg, "Integer", "integer", "int");
        register(id, LongArgument::longArg, "Long", "long");
        register(id, FloatArgument::floatArg, "Float", "float");
        register(id, DoubleArgument::doubleArg, "Double", "double");
        register(id, StringArgument::byArg, "String", "string", "str");
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
            throw BuiltExceptions.PARSE_ARGUMENT_NOT_EXIST.create(type);
        }
        Function<String, Argument<?>> factory = column.values().stream().findFirst().orElseThrow();
        return factory.apply(param);
    }

}
