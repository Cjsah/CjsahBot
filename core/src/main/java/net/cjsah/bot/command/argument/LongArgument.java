package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.CommandException;

import java.util.Optional;

public record LongArgument(long min, long max) implements Argument<Long> {

    public static LongArgument longArg(String param) {
        if (param == null) return longArg();
        int index = param.indexOf("_");
        if (index == -1) {
            return longArg(Long.parseLong(param));
        }
        long min = Long.parseLong(param.substring(0, index));
        long max = Long.parseLong(param.substring(index + 1));
        return longArg(min, max);
    }

    public static LongArgument longArg() {
        return longArg(Long.MIN_VALUE);
    }

    public static LongArgument longArg(long min) {
        return longArg(min, Long.MAX_VALUE);
    }

    public static LongArgument longArg(long min, long max) {
        return new LongArgument(min, max);
    }

    public static Optional<Long> get(CommandContext context, String name) {
        return context.getArgument(name, Long.class);
    }

    @Override
    public Long parse(final StringReader reader) throws CommandException {
        return reader.readLong();
    }
}
