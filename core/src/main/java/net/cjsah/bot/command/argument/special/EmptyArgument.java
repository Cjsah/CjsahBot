package net.cjsah.bot.command.argument.special;

import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.exception.CommandException;

public class EmptyArgument implements Argument<Void> {
    private EmptyArgument() {}

    @Override
    public Void parse(final String node) throws CommandException {
        return null;
    }
}
