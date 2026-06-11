package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;

public enum MessageSendEventType implements IStrSerializable {
//    FRIEND("private", FriendMessageEvent::new),
//    GROUP("group", GroupMessageEvent::new),
    EMPTY("empty", null),
    ;

    public static final Codec<MessageSendEventType> CODEC = IStrSerializable.fromEnum(MessageSendEventType.class);

    private final String type;
    private final Codec<?> codec;

    MessageSendEventType(String type, Codec<?> codec) {
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

    public record Builder(MessageSendEventType type) implements IEventBuilder {
        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MessageSendEventType.CODEC.fieldOf("message_type").forGetter(Builder::type)
        ).apply(instance, Builder::new));

        @Override
        public Codec<?> codec() {
            return this.type.codec;
        }
    }

}
