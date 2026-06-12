package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.data.HeartbeatStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class HeartbeatEvent extends ReceivedEvent {
    public static final Codec<HeartbeatEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        HeartbeatStatus.CODEC.fieldOf("status").forGetter(HeartbeatEvent::getStatus),
        Codec.LONG.fieldOf("interval").forGetter(HeartbeatEvent::getInterval)
    ).apply(instance, HeartbeatEvent::new));

    private final HeartbeatStatus status;
    private final long interval;

    public HeartbeatEvent(HeartbeatStatus status, long interval) {
        this.interval = interval;
        this.status = status;
    }
}
