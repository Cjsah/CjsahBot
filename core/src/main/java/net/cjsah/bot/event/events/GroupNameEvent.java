package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupNameEvent extends ReceivedEvent {
    public static final Codec<GroupNameEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupNameEvent::getGroupId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupNameEvent::getUserId),
        Codec.STRING.fieldOf("name_new").forGetter(GroupNameEvent::getName)
    ).apply(instance, GroupNameEvent::new));

    private final long groupId;
    private final long userId;
    private final String name;
}
