package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.exception.CommandException;

public record LongArgument(long min, long max) implements Argument<Long> {

    public static LongArgument longArg() {
        return longArg(Long.MIN_VALUE);
    }

    public static LongArgument longArg(long min) {
        return longArg(min, Long.MAX_VALUE);
    }

    public static LongArgument longArg(long min, long max) {
        return new LongArgument(min, max);
    }

    @Override
    public Long parse(final StringReader reader) throws CommandException {
        return reader.readLong();
    }
}
