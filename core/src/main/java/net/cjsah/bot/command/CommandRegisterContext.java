package net.cjsah.bot.command;

import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.builder.RequiredArgumentBuilder;

public final class CommandRegisterContext {
    private final CommandDispatcher dispatcher;
    private final String pluginId;

    public CommandRegisterContext(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.pluginId = "core";
    }

    public LiteralArgumentBuilder literal(String string) {
        return LiteralArgumentBuilder.literal(this.pluginId, string);
    }

    public <T> RequiredArgumentBuilder<T> argument(String string, Argument<T> argument) {
        return RequiredArgumentBuilder.argument(this.pluginId, string, argument);
    }

    public LiteralArgumentBuilder register(LiteralArgumentBuilder literal) {
        this.dispatcher.register(literal);
        return literal;
    }

}
