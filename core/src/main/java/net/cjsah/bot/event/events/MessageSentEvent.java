package net.cjsah.bot.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.enums.MessageSource;

@Getter
@RequiredArgsConstructor
public abstract class MessageSentEvent extends ReceivedEvent {
    protected final MessageSource source;
    protected final long targetId;
}
