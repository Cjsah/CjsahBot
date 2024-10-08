package net.cjsah.bot.commandV2.context;

import net.cjsah.bot.commandV2.argument.Argument;

public record CommandParameter(String name, String description, Class<? extends Argument<?>> resolver) {
}
