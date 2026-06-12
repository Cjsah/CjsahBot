package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;
import net.cjsah.bot.event.events.HeartbeatEvent;
import net.cjsah.bot.event.events.LifecycleEvent;

public enum MetaEventType implements IStrSerializable {
    HEARTBEAT("heartbeat", HeartbeatEvent.CODEC),
    LIFECYCLE("lifecycle", LifecycleEvent.CODEC),
    ;

    public static final Codec<MetaEventType> CODEC = IStrSerializable.fromEnum(MetaEventType.class);

    private final String type;
    private final Codec<?> codec;

    MetaEventType(String type, Codec<?> codec) {
        this.type = type;
        this.codec = codec;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }

    public record Builder(MetaEventType type) implements IEventBuilder {
        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MetaEventType.CODEC.fieldOf("meta_event_type").forGetter(Builder::type)
        ).apply(instance, Builder::new));

        @Override
        public Codec<?> codec() {
            return this.type.codec;
        }
    }
}
