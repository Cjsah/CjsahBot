package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FriendRequestEvent extends RequestEvent {
    public static final Codec<FriendRequestEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(FriendRequestEvent::getUserId),
        Codec.STRING.fieldOf("comment").forGetter(FriendRequestEvent::getComment),
        Codec.STRING.fieldOf("flag").forGetter(FriendRequestEvent::getFlag)
    ).apply(instance, FriendRequestEvent::new));

    public FriendRequestEvent(long userId, String comment, String flag) {
        super(userId, comment, flag);
    }
}
