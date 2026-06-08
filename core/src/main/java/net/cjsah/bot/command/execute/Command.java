package net.cjsah.bot.command.execute;

import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.exception.CommandException;

public interface Command {

    int run(CommandContext context) throws CommandException;
}
