package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.data.enums.LifecycleStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class LifecycleEvent extends ReceivedEvent {

    public static final Codec<LifecycleEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        LifecycleStatus.CODEC.fieldOf("sub_type").forGetter(LifecycleEvent::getStatus)
    ).apply(instance, LifecycleEvent::new));

    private final LifecycleStatus status;
}
