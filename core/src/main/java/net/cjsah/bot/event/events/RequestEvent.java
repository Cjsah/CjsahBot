package net.cjsah.bot.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class RequestEvent extends ReceivedEvent {
    protected final long userId;
    protected final String comment;
    protected final String flag;
}
