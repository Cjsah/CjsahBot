package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;
import net.cjsah.bot.event.events.FriendMessageEvent;
import net.cjsah.bot.event.events.GroupMessageEvent;

public enum MessageEventType implements IStrSerializable {
    FRIEND("private", FriendMessageEvent.CODEC),
    GROUP("group", GroupMessageEvent.CODEC),
    EMPTY("empty", null),
    ;

    public static final Codec<MessageEventType> CODEC = IStrSerializable.fromEnum(MessageEventType.class);

    private final String type;
    private final Codec<?> codec;

    MessageEventType(String type, Codec<?> codec) {
        this.type = type;
        this.codec = codec;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }

    public record Builder(MessageEventType type) implements IEventBuilder {
        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MessageEventType.CODEC.fieldOf("message_type").forGetter(Builder::type)
        ).apply(instance, Builder::new));

        @Override
        public Codec<?> codec() {
            return this.type.codec;
        }
    }

}
