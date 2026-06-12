package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FriendAddEvent extends ReceivedEvent {
    public static final Codec<FriendAddEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(FriendAddEvent::getUserId)
    ).apply(instance, FriendAddEvent::new));

    private final long userId;
}
