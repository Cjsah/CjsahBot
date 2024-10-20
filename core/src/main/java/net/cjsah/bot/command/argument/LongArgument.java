package net.cjsah.bot.command.argument;

import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public class LongArgument implements Argument<Long> {
    @Override
    public Long parse(final String node) throws CommandException {
        if (node.isEmpty()) {
            throw BuiltExceptions.READER_EXPECTED_LONG.create();
        }
        try {
            return Long.parseLong(node);
        }catch (NumberFormatException e) {
            throw BuiltExceptions.READER_INVALID_LONG.create(node);
        }
    }
}
