package net.cjsah.bot.command.argument;

import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public class ByteArgument implements Argument<Byte> {
    @Override
    public Byte parse(final String node) throws CommandException {
        if (node.isEmpty()) {
            throw BuiltExceptions.READER_EXPECTED_BYTE.create();
        }
        try {
            return Byte.parseByte(node);
        }catch (NumberFormatException e) {
            throw BuiltExceptions.READER_INVALID_BYTE.create(node);
        }
    }
}
