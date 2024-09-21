package net.cjsah.bot.commandV2.argument;

import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public class FloatArgument implements Argument<Float> {
    @Override
    public Float parse(final String node) throws CommandException {
        if (node.isEmpty()) {
            throw BuiltExceptions.READER_EXPECTED_FLOAT.create();
        }
        try {
            return Float.parseFloat(node);
        }catch (NumberFormatException e) {
            throw BuiltExceptions.READER_INVALID_FLOAT.create(node);
        }
    }

}
