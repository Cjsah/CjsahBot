package net.cjsah.bot.command;

import net.cjsah.bot.command.context.ContextBuilder;
import net.cjsah.bot.command.tree.CommandNode;
import net.cjsah.bot.exception.CommandException;

import java.util.Map;

public record ParseResults(
        ContextBuilder context,
        StringReader reader,
        Map<CommandNode, CommandException> exceptions
) { }
