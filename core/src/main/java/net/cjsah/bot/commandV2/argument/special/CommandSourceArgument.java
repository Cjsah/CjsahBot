package net.cjsah.bot.commandV2.argument.special;

import net.cjsah.bot.commandV2.argument.Argument;
import net.cjsah.bot.exception.CommandException;

import java.util.Map;

public class CommandSourceArgument implements Argument<Void> { // special
    private CommandSourceArgument() {}

    @Override
    public Void parse(final String node) throws CommandException {
        return null;
    }
}
