package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;

public enum GroupAdminChangeEventType implements IStrSerializable {
//    SET("set", GroupAdminSetEvent::new),
//    UNSET("unset", GroupAdminUnsetEvent::new),
    EMPTY("empty", null),
    ;

    public static final Codec<GroupAdminChangeEventType> CODEC = IStrSerializable.fromEnum(GroupAdminChangeEventType.class);

    private final String type;
    private final Codec<?> codec;

    GroupAdminChangeEventType(String type, Codec<?> codec) {
        this.type = type;
        this.codec = codec;
    }

    @Override
    public String getSerializedName() {
        return this.type;
    }

    public record Builder(GroupAdminChangeEventType type) implements IEventBuilder {
        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GroupAdminChangeEventType.CODEC.fieldOf("meta_event_type").forGetter(Builder::type)
        ).apply(instance, Builder::new));

        @Override
        public Codec<?> codec() {
            return this.type.codec;
        }
    }
}
