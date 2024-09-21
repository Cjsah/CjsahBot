package net.cjsah.bot.commandV2.argument;

import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.CommandException;

public class BooleanArgument implements Argument<Boolean> {
    @Override
    public Boolean parse(final String node) throws CommandException {
        if (node.isEmpty()) {
            throw BuiltExceptions.READER_EXPECTED_BOOL.create();
        }
        return switch (node) {
            case "true" -> true;
            case "false" -> false;
            default -> throw BuiltExceptions.READER_INVALID_BOOL.create(node);
        };
    }
}
