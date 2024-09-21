package net.cjsah.bot.commandV2.argument;

import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public class DoubleArgument implements Argument<Double> {
    @Override
    public Double parse(final String node) throws CommandException {
        if (node.isEmpty()) {
            throw BuiltExceptions.READER_EXPECTED_DOUBLE.create();
        }
        try {
            return Double.parseDouble(node);
        }catch (NumberFormatException e) {
            throw BuiltExceptions.READER_INVALID_DOUBLE.create(node);
        }
    }
}
