package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.data.enums.MessageSource;

@Data
@EqualsAndHashCode(callSuper = true)
public class FriendMessageSentEvent extends MessageSentEvent {
    public static final Codec<FriendMessageSentEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("target_id").forGetter(FriendMessageSentEvent::getTargetId)
    ).apply(instance, FriendMessageSentEvent::new));

    public FriendMessageSentEvent(long targetId) {
        super(MessageSource.FRIEND, targetId);
    }
}