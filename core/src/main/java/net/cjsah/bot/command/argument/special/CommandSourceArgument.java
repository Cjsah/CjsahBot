package net.cjsah.bot.command.argument.special;

import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.exception.CommandException;

public class CommandSourceArgument implements Argument<Void> { // special
    private CommandSourceArgument() {}

    @Override
    public Void parse(final String node) throws CommandException {
        return null;
    }
}
