package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.exception.CommandException;

public class BooleanArgument implements Argument<Boolean> {
    private BooleanArgument() {
    }

    public static BooleanArgument bool() {
        return new BooleanArgument();
    }

    @Override
    public Boolean parse(final StringReader reader) throws CommandException {
        return reader.readBoolean();
    }
}
