package net.cjsah.bot.command.argument;

import net.cjsah.bot.exception.CommandException;

public class StringArgument implements Argument<String> {
    @Override
    public String parse(final String node) throws CommandException {
        return node;
    }
}
