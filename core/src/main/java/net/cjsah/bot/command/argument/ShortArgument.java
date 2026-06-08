package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.exception.CommandException;

public record ShortArgument(short min, short max) implements Argument<Short> {

    public static ShortArgument shortArg() {
        return shortArg(Short.MIN_VALUE);
    }

    public static ShortArgument shortArg(short min) {
        return shortArg(min, Short.MAX_VALUE);
    }

    public static ShortArgument shortArg(short min, short max) {
        return new ShortArgument(min, max);
    }

    @Override
    public Short parse(final StringReader reader) throws CommandException {
        return reader.readShort();
    }
}
