package net.cjsah.bot.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.data.BaseUserData;
import net.cjsah.bot.data.enums.MessageSource;

@Getter
@RequiredArgsConstructor
public abstract class MessageSentEvent<T extends BaseUserData> extends ReceivedEvent {
    protected final MessageSource source;
    protected final long targetId;
    protected final MessageChain message;
    protected final T sender;
}
