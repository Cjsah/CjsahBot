package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.CommandException;

import java.util.Optional;

public record ShortArgument(short min, short max) implements Argument<Short> {

    public static ShortArgument shortArg(String param) {
        if (param == null) return shortArg();
        int index = param.indexOf("_");
        if (index == -1) {
            return shortArg(Short.parseShort(param));
        }
        short min = Short.parseShort(param.substring(0, index));
        short max = Short.parseShort(param.substring(index + 1));
        return shortArg(min, max);
    }

    public static ShortArgument shortArg() {
        return shortArg(Short.MIN_VALUE);
    }

    public static ShortArgument shortArg(short min) {
        return shortArg(min, Short.MAX_VALUE);
    }

    public static ShortArgument shortArg(short min, short max) {
        return new ShortArgument(min, max);
    }

    public static Optional<Short> get(CommandContext context, String name) {
        return context.getArgument(name, Short.class);
    }

    @Override
    public Short parse(final StringReader reader) throws CommandException {
        return reader.readShort();
    }
}
