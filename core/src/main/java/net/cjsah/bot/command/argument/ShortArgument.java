package net.cjsah.bot.command.argument;

import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public class ShortArgument implements Argument<Short> {
    @Override
    public Short parse(final String node) throws CommandException {
        if (node.isEmpty()) {
            throw BuiltExceptions.READER_EXPECTED_SHORT.create();
        }
        try {
            return Short.parseShort(node);
        }catch (NumberFormatException e) {
            throw BuiltExceptions.READER_INVALID_SHORT.create(node);
        }
    }
}
