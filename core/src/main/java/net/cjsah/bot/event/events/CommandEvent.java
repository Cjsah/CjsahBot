package net.cjsah.bot.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.command.context.CommandContext;

@Getter
@RequiredArgsConstructor
public class CommandEvent extends CancelableEvent {
    private final CommandContext context;
}
