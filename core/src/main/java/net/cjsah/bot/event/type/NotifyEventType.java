package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;

public enum NotifyEventType implements IStrSerializable {
//    POKE("poke", GroupPokeEvent::new),
//    LUCKY_KING("lucky_king", GroupLuckyKingEvent::new),
//    HONOR("honor", GroupHonorEvent::new),
    EMPTY("empty", null),
    ;

    public static final Codec<NotifyEventType> CODEC = IStrSerializable.fromEnum(NotifyEventType.class);

    private final String type;
    private final Codec<?> codec;

    NotifyEventType(String type, Codec<?> codec) {
        this.type = type;
        this.codec = codec;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }

    public record Builder(NotifyEventType type) implements IEventBuilder {
        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            NotifyEventType.CODEC.fieldOf("meta_event_type").forGetter(Builder::type)
        ).apply(instance, Builder::new));

        @Override
        public Codec<?> codec() {
            return this.type.codec;
        }
    }

}
