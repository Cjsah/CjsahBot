package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;

public enum MessageSentEventType implements IStrSerializable {
//    FRIEND("private", FriendMessageEvent::new),
//    GROUP("group", GroupMessageEvent::new),
    EMPTY("empty", null),
    ;

    public static final Codec<MessageSentEventType> CODEC = IStrSerializable.fromEnum(MessageSentEventType.class);

    private final String type;
    private final Codec<?> codec;

    MessageSentEventType(String type, Codec<?> codec) {
        this.type = type;
        this.codec = codec;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }

    public record Builder(MessageSentEventType type) implements IEventBuilder {
        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MessageSentEventType.CODEC.fieldOf("message_type").forGetter(Builder::type)
        ).apply(instance, Builder::new));

        @Override
        public Codec<?> codec() {
            return this.type.codec;
        }
    }

}
