package net.cjsah.bot.event.events;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.util.ExtraCodecs;

@Data
@EqualsAndHashCode(callSuper = true)
public class FriendPokeEvent extends PokeEvent {
    public static final Codec<FriendPokeEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(FriendPokeEvent::getUserId),
        Codec.LONG.fieldOf("target_id").forGetter(FriendPokeEvent::getTargetId),
        Codec.LONG.fieldOf("sender_id").forGetter(FriendPokeEvent::getSenderId),
        ExtraCodecs.JSON.fieldOf("raw_info").forGetter(FriendPokeEvent::getRawInfo)
    ).apply(instance, FriendPokeEvent::new));

    private final long senderId;
    private final JsonElement rawInfo;

    public FriendPokeEvent(long userId, long targetId, long senderId, JsonElement rawInfo) {
        super(userId, targetId);
        this.senderId = senderId;
        this.rawInfo = rawInfo;
    }
}
