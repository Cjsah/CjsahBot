package net.cjsah.bot.event.events;

import lombok.Getter;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.command.tree.CommandNode;

@Getter
public class CommandEvent extends CancelableEvent {
    private final CommandContext context;
    private final CommandNode commandNode;

    public CommandEvent(CommandContext context, CommandNode commandNode) {
        this.context = context;
        this.commandNode = commandNode;
    }

}
