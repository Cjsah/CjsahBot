package net.cjsah.bot.commandV2.argument;

import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public class IntArgument implements Argument<Integer> {
    public static IntArgument INSTANCE = new IntArgument();

    private IntArgument() {}

    @Override
    public Integer parse(final String node) throws CommandException {
        if (node.isEmpty()) {
            throw BuiltExceptions.READER_EXPECTED_INT.create();
        }
        try {
            return Integer.parseInt(node);
        }catch (NumberFormatException e) {
            throw BuiltExceptions.READER_INVALID_INT.create(node);
        }
    }
}
