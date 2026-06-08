package net.cjsah.bot.command;

import net.cjsah.bot.command.context.CommandContextBuilder;
import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.exception.CommandException;

import java.util.Collections;
import java.util.Map;

public record ParseResults(CommandContextBuilder context, StringReader reader, Map<CommandNode, CommandException> exceptions) {
    public ParseResults(final CommandContextBuilder context) {
        this(context, new StringReader(""), Collections.emptyMap());
    }
}

