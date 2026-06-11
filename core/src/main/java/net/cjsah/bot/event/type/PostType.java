package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;

public enum PostType implements IStrSerializable, IEventBuilder {
    META("meta_event", MetaEventType.Builder.CODEC),
    NOTICE("notice", NoticeEventType.Builder.CODEC),
    REQUEST("request", RequestEventType.Builder.CODEC),
    MESSAGE("message", MessageEventType.Builder.CODEC),
    MESSAGE_SENT("message_sent", MessageEventType.Builder.CODEC),
    ;

    public static final Codec<PostType> CODEC = IStrSerializable.fromEnum(PostType.class);

    private final String type;
    private final Codec<?> codec;

    PostType(String type, Codec<?> codec) {
        this.type = type;
        this.codec = codec;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }

    public Codec<?> codec() {
        return this.codec;
    }
}
