package net.cjsah.bot.event.type;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;
import net.cjsah.bot.event.events.BaseEvent;

public enum PostType implements IStrSerializable, IEventBuilder {
    META("meta_event", MetaEventType.Builder.CODEC),
    REQUEST("request", RequestEventType.Builder.CODEC),
    NOTICE("notice", NoticeEventType.Builder.CODEC),
    MESSAGE("message", MessageEventType.Builder.CODEC),
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

    public Either<Codec<? extends IEventBuilder>, Codec<? extends BaseEvent>> codec() {
        return Either.left(this.codec);
    }
}
