package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class InputStatusEvent extends ReceivedEvent {
    public static final Codec<InputStatusEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.optionalFieldOf("group_id").forGetter(InputStatusEvent::getGroupId),
        Codec.LONG.fieldOf("user_id").forGetter(InputStatusEvent::getUserId),
        Codec.INT.fieldOf("event_type").forGetter(InputStatusEvent::getEventType),
        Codec.STRING.fieldOf("status_text").forGetter(InputStatusEvent::getText)
    ).apply(instance, InputStatusEvent::new));

    private final Optional<Long> groupId;
    private final long userId;
    private final int eventType;
    private final String text;
}
