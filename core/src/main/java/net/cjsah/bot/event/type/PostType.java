package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;

public enum PostType implements IStrSerializable {
    META("meta_event", MetaEventType.Builder.CODEC),
    MESSAGE("message", MessageEventType.Builder.CODEC),
    REQUEST("request", RequestEventType.Builder.CODEC),
    NOTICE("notice", NoticeEventType.Builder.CODEC),
    MESSAGE_SENT("message_sent", null),
    ;

    public static final Codec<PostType> CODEC = IStrSerializable.fromEnum(PostType.class);

    private final String type;
    private final Codec<? extends IEventBuilder> codec;

    PostType(String type, Codec<? extends IEventBuilder> codec) {
        this.type = type;
        this.codec = codec;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }

    public Codec<? extends IEventBuilder> codec() {
        return this.codec;
    }
}
