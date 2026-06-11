package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;

public enum RequestEventType implements IStrSerializable {
//    FRIEND("friend", FriendRequestEvent::new),
//    GROUP("group", GroupRequestEvent::new),
    EMPTY("empty", null),
    ;

    public static final Codec<RequestEventType> CODEC = IStrSerializable.fromEnum(RequestEventType.class);

    private final String type;
    private final Codec<?> codec;

    RequestEventType(String type, Codec<?> codec) {
        this.type = type;
        this.codec = codec;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }

    public record Builder(RequestEventType type) implements IEventBuilder {
        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RequestEventType.CODEC.fieldOf("request_type").forGetter(Builder::type)
        ).apply(instance, Builder::new));

        @Override
        public Codec<?> codec() {
            return this.type.codec;
        }
    }

}
