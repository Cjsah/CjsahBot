package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.exception.CommandException;

public record IntArgument(int min, int max) implements Argument<Integer> {

    public static IntArgument intArg() {
        return intArg(Integer.MIN_VALUE);
    }

    public static IntArgument intArg(int min) {
        return intArg(min, Integer.MAX_VALUE);
    }

    public static IntArgument intArg(int min, int max) {
        return new IntArgument(min, max);
    }

    @Override
    public Integer parse(final StringReader reader) throws CommandException {
        return reader.readInt();
    }
}
