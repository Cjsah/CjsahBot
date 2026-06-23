package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.CommandException;

import java.util.Optional;

public class BooleanArgument implements Argument<Boolean> {
    private BooleanArgument() {
    }

    public static BooleanArgument bool() {
        return new BooleanArgument();
    }

    public static Optional<Boolean> get(CommandContext context, String name) {
        return context.getArgument(name, Boolean.class);
    }

    @Override
    public Boolean parse(final StringReader reader) throws CommandException {
        return reader.readBoolean();
    }
}
