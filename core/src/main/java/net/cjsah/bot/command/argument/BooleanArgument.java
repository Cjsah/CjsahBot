package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.CommandException;

public class BooleanArgument implements Argument<Boolean> {

    public static BooleanArgument bool() {
        return new BooleanArgument();
    }

    public static boolean getBoolean(final CommandContext context, final String name) throws CommandException {
        return context.getArgument(name, boolean.class);
    }

    @Override
    public Boolean parse(final StringReader reader) throws CommandException {
        return reader.readBoolean();
    }
}
