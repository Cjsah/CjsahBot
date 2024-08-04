package net.cjsah.bot.command.argument;

import net.cjsah.bot.command.StringReader;
import net.cjsah.bot.exception.CommandException;

public interface Argument<T> {
    T parse(final StringReader reader) throws CommandException;
}
