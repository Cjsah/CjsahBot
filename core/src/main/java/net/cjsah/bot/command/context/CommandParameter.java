package net.cjsah.bot.command.context;

import net.cjsah.bot.command.argument.Argument;

public record CommandParameter(String name, String description, Class<? extends Argument<?>> resolver) {
}
