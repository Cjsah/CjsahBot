package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupMemberTitleEvent extends ReceivedEvent {
    public static final Codec<GroupMemberTitleEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupMemberTitleEvent::getGroupId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupMemberTitleEvent::getUserId),
        Codec.STRING.fieldOf("name_new").forGetter(GroupMemberTitleEvent::getName)
    ).apply(instance, GroupMemberTitleEvent::new));

    private final long groupId;
    private final long userId;
    private final String name;
}
