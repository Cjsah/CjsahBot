package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.exception.CommandException;

public record DoubleArgument(double min, double max) implements Argument<Double> {

    public static DoubleArgument doubleArg() {
        return doubleArg(Double.MIN_VALUE);
    }

    public static DoubleArgument doubleArg(double min) {
        return doubleArg(min, Double.MAX_VALUE);
    }

    public static DoubleArgument doubleArg(double min, double max) {
        return new DoubleArgument(min, max);
    }

    @Override
    public Double parse(final StringReader reader) throws CommandException {
        return reader.readDouble();
    }
}
