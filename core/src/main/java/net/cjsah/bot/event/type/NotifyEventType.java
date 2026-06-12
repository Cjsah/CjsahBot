package net.cjsah.bot.event.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.IEventBuilder;
import net.cjsah.bot.data.IStrSerializable;
import net.cjsah.bot.event.events.GroupGrayTipEvent;
import net.cjsah.bot.event.events.GroupMemberTitleEvent;
import net.cjsah.bot.event.events.GroupNameEvent;
import net.cjsah.bot.event.events.InputStatusEvent;
import net.cjsah.bot.event.events.PokeEvent;
import net.cjsah.bot.event.events.ProfileLikeEvent;

public enum NotifyEventType implements IStrSerializable {
    GROUP_NAME("group_name", GroupNameEvent.CODEC),
    TITLE("title", GroupMemberTitleEvent.CODEC),
    GRAY_TIP("gray_tip", GroupGrayTipEvent.CODEC),
    POKE("poke", PokeEvent.Builder.CODEC),
    PROFILE_LIKE("profile_like", ProfileLikeEvent.CODEC),
    INPUT_STATUS("input_status", InputStatusEvent.CODEC),
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
            NotifyEventType.CODEC.fieldOf("sub_type").forGetter(Builder::type)
        ).apply(instance, Builder::new));

        @Override
        public Codec<?> codec() {
            return this.type.codec;
        }
    }

}
