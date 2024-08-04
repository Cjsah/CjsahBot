package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public class LongArgument implements Argument<Long> {
    private final long min;
    private final long max;

    public LongArgument(long min, long max) {
        this.min = min;
        this.max = max;
    }

    public static LongArgument longArg() {
        return new LongArgument(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static LongArgument longArg(long min) {
        return new LongArgument(min, Long.MAX_VALUE);
    }

    public static LongArgument longArg(long min, long max) {
        return new LongArgument(min, max);
    }

    public static float getLong(CommandContext context, String name) {
        return context.getArgument(name, long.class);
    }

    public long getMin() {
        return this.min;
    }

    public long getMax() {
        return this.max;
    }

    @Override
    public Long parse(StringReader reader) throws CommandException {
        int start = reader.getCursor();
        long result = reader.readLong();
        if (result < this.min) {
            reader.setCursor(start);
            throw BuiltExceptions.LONG_TOO_LOW.create(result, this.min);
        }
        if (result > this.max) {
            reader.setCursor(start);
            throw BuiltExceptions.LONG_TOO_HIGH.create(result, this.max);
        }
        return result;
    }
}
