package net.cjsah.bot.command;

import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.CommandException;

public interface Command {
    void run(CommandContext context) throws CommandException;
}
