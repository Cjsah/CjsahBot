package net.cjsah.bot.command;

import net.cjsah.bot.command.context.CommandContextBuilder;
import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.exception.CommandException;

import java.util.Collections;
import java.util.Map;

public record ParseResults<S>(CommandContextBuilder<S> context, StringReader reader, Map<CommandNode<S>, CommandException> exceptions) {
    public ParseResults(final CommandContextBuilder<S> context) {
        this(context, new StringReader(""), Collections.emptyMap());
    }
}

