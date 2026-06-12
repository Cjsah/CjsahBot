package net.cjsah.bot.event.events;

import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.data.BaseUserData;
import net.cjsah.bot.data.enums.MessageType;

@Getter
@RequiredArgsConstructor
public abstract class MessageEvent<T extends BaseUserData> extends ReceivedEvent {
    protected final long messageId;
    protected final long userId;
    protected final JsonElement message;
    protected final String rawMessage;
    protected final T sender;
    protected final MessageType type;

    public abstract CommandSource<?> genCommandSource();
}
