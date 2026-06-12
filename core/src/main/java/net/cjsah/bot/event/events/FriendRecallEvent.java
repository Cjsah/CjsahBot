package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class FriendRecallEvent extends MessageRecallEvent {
    public static final Codec<FriendRecallEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(FriendRecallEvent::getUserId),
        Codec.LONG.fieldOf("message_id").forGetter(FriendRecallEvent::getMessageId)
    ).apply(instance, FriendRecallEvent::new));

    public FriendRecallEvent(long userId, long messageId) {
        super(userId, messageId);
    }
}
