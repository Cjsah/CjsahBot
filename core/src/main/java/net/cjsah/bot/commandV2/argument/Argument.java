package net.cjsah.bot.commandV2.argument;

import net.cjsah.bot.commandV2.CommandManager;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public interface Argument<T> {

    T parse(final String node) throws CommandException;

    static <CLASS extends Class<?>> Argument<?> generate(CLASS clazz) {
        switch (clazz.getTypeName()) {
            case "boolean":
                CommandManager.log.warn("基本类型对可选内容兼容性较差, 建议使用包装类型");
            case "java.lang.Boolean":
                return BooleanArgument.INSTANCE;
            case "byte":
                CommandManager.log.warn("基本类型对可选内容兼容性较差, 建议使用包装类型");
            case "java.lang.Byte":
                return ByteArgument.INSTANCE;
            case "short":
                CommandManager.log.warn("基本类型对可选内容兼容性较差, 建议使用包装类型");
            case "java.lang.Short":
                return ShortArgument.INSTANCE;
            case "int":
                CommandManager.log.warn("基本类型对可选内容兼容性较差, 建议使用包装类型");
            case"java.lang.Integer":
                return IntArgument.INSTANCE;
            case "long":
                CommandManager.log.warn("基本类型对可选内容兼容性较差, 建议使用包装类型");
            case"java.lang.Long":
                return LongArgument.INSTANCE;
            case "float":
                CommandManager.log.warn("基本类型对可选内容兼容性较差, 建议使用包装类型");
            case"java.lang.Float":
                return FloatArgument.INSTANCE;
            case "double":
                CommandManager.log.warn("基本类型对可选内容兼容性较差, 建议使用包装类型");
            case"java.lang.Double":
                return DoubleArgument.INSTANCE;
            case "java.lang.String":
                return StringArgument.INSTANCE;
            case "java.util.Map":
                return ArgsArgument.INSTANCE;
            default: throw BuiltExceptions.UNSUPPORTED_TYPE.create(clazz.getTypeName());
        }
    }
}
