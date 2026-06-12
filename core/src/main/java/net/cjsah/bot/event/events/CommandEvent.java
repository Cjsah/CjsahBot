package net.cjsah.bot.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.command.context.CommandContext;
import net.cjsah.bot.command.tree.CommandNode;

@Getter
@RequiredArgsConstructor
public class CommandEvent extends CancelableEvent {
    private final CommandContext context;
    private final CommandNode commandNode;
}
