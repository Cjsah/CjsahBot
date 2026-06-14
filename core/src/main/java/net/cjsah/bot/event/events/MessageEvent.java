package net.cjsah.bot.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.data.BaseUserData;
import net.cjsah.bot.data.enums.MessageSource;

@Getter
@RequiredArgsConstructor
public abstract class MessageEvent<T extends BaseUserData> extends ReceivedEvent {
    protected final long messageId;
    protected final long userId;
    protected final MessageChain message;
    protected final String rawMessage;
    protected final T sender;
    protected final MessageSource type;

    public abstract CommandSource<?> getCommandSource();
}
