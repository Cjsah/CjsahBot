package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;
import net.cjsah.bot.event.events.BaseEvent;

public enum MetaEventType implements IStrSerializable, IEventBuilder {
//    META("lifecycle", LifecycleEvent::new),
//    MESSAGE("heartbeat", HeartbeatEvent::new),
    EMPTY("empty", null),
    ;

    public static final Codec<MetaEventType> CODEC = IStrSerializable.fromEnum(MetaEventType.class);

    private final String type;
    private final Codec<? extends BaseEvent> codec;

    MetaEventType(String type, Codec<? extends BaseEvent> codec) {
        this.type = type;
        this.codec = codec;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }

    @Override
    public Codec<? extends BaseEvent> codec() {
        return this.codec;
    }

    public record Builder(MetaEventType type) implements IEventBuilder {
        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MetaEventType.CODEC.fieldOf("meta_event_type").forGetter(Builder::type)
        ).apply(instance, Builder::new));

        @Override
        public Codec<? extends BaseEvent> codec() {
            return this.type.codec();
        }
    }
}
