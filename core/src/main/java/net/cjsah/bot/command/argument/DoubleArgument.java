package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.CommandException;

import java.util.Optional;

public record DoubleArgument(double min, double max) implements Argument<Double> {

    public static DoubleArgument doubleArg(String param) {
        if (param == null) return doubleArg();
        int index = param.indexOf("_");
        if (index == -1) {
            return doubleArg(Double.parseDouble(param));
        }
        double min = Double.parseDouble(param.substring(0, index));
        double max = Double.parseDouble(param.substring(index + 1));
        return doubleArg(min, max);
    }

    public static DoubleArgument doubleArg() {
        return doubleArg(Double.MIN_VALUE);
    }

    public static DoubleArgument doubleArg(double min) {
        return doubleArg(min, Double.MAX_VALUE);
    }

    public static DoubleArgument doubleArg(double min, double max) {
        return new DoubleArgument(min, max);
    }

    public static Optional<Double> get(CommandContext context, String name) {
        return context.getArgument(name, Double.class);
    }

    @Override
    public Double parse(final StringReader reader) throws CommandException {
        return reader.readDouble();
    }
}
