package net.cjsah.bot.commandV2.argument;

import net.cjsah.bot.exception.CommandException;

public interface Argument<T> {
    T parse(final String node) throws CommandException;
}
