package net.cjsah.bot.command.argument.special;

import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.exception.CommandException;

public class ArgsArgument implements Argument<Void> { // special
    private ArgsArgument() {}

    @Override
    public Void parse(final String node) throws CommandException {
        return null;
    }
}
