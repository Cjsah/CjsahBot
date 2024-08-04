package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public class IntArgument implements Argument<Integer> {
    private final int min;
    private final int max;

    public IntArgument(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static IntArgument integer() {
        return new IntArgument(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static IntArgument integer(int min) {
        return new IntArgument(min, Integer.MAX_VALUE);
    }

    public static IntArgument integer(int min, int max) {
        return new IntArgument(min, max);
    }

    public static float getInteger(CommandContext context, String name) {
        return context.getArgument(name, int.class);
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

    @Override
    public Integer parse(StringReader reader) throws CommandException {
        int start = reader.getCursor();
        int result = reader.readInt();
        if (result < this.min) {
            reader.setCursor(start);
            throw BuiltExceptions.INTEGER_TOO_LOW.create(result, this.min);
        }
        if (result > this.max) {
            reader.setCursor(start);
            throw BuiltExceptions.INTEGER_TOO_HIGH.create(result, this.max);
        }
        return result;
    }
}
