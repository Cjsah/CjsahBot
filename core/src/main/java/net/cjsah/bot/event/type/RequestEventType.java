package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;
import net.cjsah.bot.event.events.BaseEvent;

public enum RequestEventType implements IStrSerializable, IEventBuilder {
//    FRIEND("friend", FriendRequestEvent::new),
//    GROUP("group", GroupRequestEvent::new),
    EMPTY("empty", null),
    ;

    public static final Codec<RequestEventType> CODEC = IStrSerializable.fromEnum(RequestEventType.class);

    private final String type;
    private final Codec<? extends BaseEvent> codec;

    RequestEventType(String type, Codec<? extends BaseEvent> codec) {
        this.type = type;
        this.codec = codec;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }

    @Override
    public Codec<? extends BaseEvent> codec() {
        return this.codec;
    }

    public record Builder(RequestEventType type) implements IEventBuilder {
        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RequestEventType.CODEC.fieldOf("meta_event_type").forGetter(Builder::type)
        ).apply(instance, Builder::new));

        @Override
        public Codec<? extends BaseEvent> codec() {
            return this.type.codec();
        }
    }

}
