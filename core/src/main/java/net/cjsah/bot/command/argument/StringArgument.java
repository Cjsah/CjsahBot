package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.CommandException;

public class StringArgument implements Argument<String> {
    private final boolean space;

    public StringArgument(boolean space) {
        this.space = space;
    }

    public static StringArgument word() {
        return new StringArgument(false);
    }

    public static StringArgument string() {
        return new StringArgument(true);
    }

    public String getString(CommandContext context, String name) {
        return context.getArgument(name, String.class);
    }


    @Override
    public String parse(StringReader reader) throws CommandException {
        if (this.space) {
            String remaining = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            return remaining;
        } else {
            return reader.readQuotedString();
        }
    }
}
