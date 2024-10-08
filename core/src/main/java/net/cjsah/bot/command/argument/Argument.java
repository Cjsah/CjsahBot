package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.argument.special.ArgsArgument;
import net.cjsah.bot.command.argument.special.CommandSourceArgument;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public interface Argument<T> {

    T parse(final String node) throws CommandException;

    static Class<? extends Argument<?>> getResolver(Class<?> clazz) {
        return switch (clazz.getTypeName()) {
            case "boolean", "java.lang.Boolean" -> BooleanArgument.class;
            case "byte", "java.lang.Byte" -> ByteArgument.class;
            case "short", "java.lang.Short" -> ShortArgument.class;
            case "int", "java.lang.Integer" -> IntArgument.class;
            case "long", "java.lang.Long" -> LongArgument.class;
            case "float", "java.lang.Float" -> FloatArgument.class;
            case "double", "java.lang.Double" -> DoubleArgument.class;
            case "java.lang.String" -> StringArgument.class;
            case "java.util.Map" -> ArgsArgument.class;
            default -> {
                if (CommandSource.class.isAssignableFrom(clazz)) {
                    yield CommandSourceArgument.class;
                }
                throw BuiltExceptions.UNSUPPORTED_TYPE.create(clazz.getTypeName());
            }
        };
    }
}
