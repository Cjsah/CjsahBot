package net.cjsah.bot.command;

import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.builder.RequiredArgumentBuilder;
import net.cjsah.bot.command.source.CommandSource;

public final class CommandRegisterContext {
    private final CommandDispatcher<CommandSource<?>> dispatcher;
    private final String pluginId;

    public CommandRegisterContext(CommandDispatcher<CommandSource<?>> dispatcher) {
        this.dispatcher = dispatcher;
        this.pluginId = "core";
    }

    public LiteralArgumentBuilder<CommandSource<?>> literal(String string) {
        return LiteralArgumentBuilder.literal(this.pluginId, string);
    }

    public <T> RequiredArgumentBuilder<CommandSource<?>, T> argument(String string, Argument<T> argument) {
        return RequiredArgumentBuilder.argument(this.pluginId, string, argument);
    }

    public void register() {

    }

}
