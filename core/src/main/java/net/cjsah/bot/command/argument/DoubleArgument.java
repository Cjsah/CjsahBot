package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public class DoubleArgument implements Argument<Double> {
    private final double min;
    private final double max;

    public DoubleArgument(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public static DoubleArgument doubleArg() {
        return new DoubleArgument(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static DoubleArgument doubleArg(double min) {
        return new DoubleArgument(min, Double.MAX_VALUE);
    }

    public static DoubleArgument doubleArg(double min, double max) {
        return new DoubleArgument(min, max);
    }

    public static double getDouble(CommandContext context, String name) {
        return context.getArgument(name, double.class);
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    @Override
    public Double parse(StringReader reader) throws CommandException {
        int start = reader.getCursor();
        double result = reader.readDouble();
        if (result < this.min) {
            reader.setCursor(start);
            throw BuiltExceptions.DOUBLE_TOO_LOW.create(result, this.min);
        }
        if (result > this.max) {
            reader.setCursor(start);
            throw BuiltExceptions.DOUBLE_TOO_HIGH.create(result, this.max);
        }
        return result;
    }
}
