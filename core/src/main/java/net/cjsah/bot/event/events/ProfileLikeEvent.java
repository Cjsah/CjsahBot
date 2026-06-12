package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProfileLikeEvent extends ReceivedEvent {
    public static final Codec<ProfileLikeEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("operator_id").forGetter(ProfileLikeEvent::getOperatorId),
        Codec.STRING.fieldOf("operator_nick").forGetter(ProfileLikeEvent::getOperatorNick),
        Codec.INT.fieldOf("times").forGetter(ProfileLikeEvent::getTimes),
        Codec.LONG.fieldOf("time").forGetter(ProfileLikeEvent::getEpochSeconds)
    ).apply(instance, ProfileLikeEvent::new));

    private final long operatorId;
    private final String operatorNick;
    private final int times;
    private final Instant time;

    public ProfileLikeEvent(long operatorId, String operatorNick, int times, long time) {
        this.operatorId = operatorId;
        this.operatorNick = operatorNick;
        this.times = times;
        this.time = Instant.ofEpochSecond(time);
    }

    public long getEpochSeconds() {
        return this.time.getEpochSecond();
    }
}
