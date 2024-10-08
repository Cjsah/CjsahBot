package net.cjsah.bot.commandV2.argument;

import net.cjsah.bot.exception.CommandException;

public class StringArgument implements Argument<String> {
    @Override
    public String parse(final String node) throws CommandException {
        return node;
    }
}
