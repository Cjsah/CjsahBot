package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupRequestEvent extends RequestEvent {

    public static final Codec<GroupRequestEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(GroupRequestEvent::getUserId),
        Codec.STRING.fieldOf("comment").forGetter(GroupRequestEvent::getComment),
        Codec.STRING.fieldOf("flag").forGetter(GroupRequestEvent::getFlag),
        Codec.STRING.fieldOf("sub_type").forGetter(GroupRequestEvent::getType)
    ).apply(instance, GroupRequestEvent::new));

    private final String type;

    public GroupRequestEvent(long userId, String comment, String flag, String type) {
        super(userId, comment, flag);
        this.type = type;
    }

}
