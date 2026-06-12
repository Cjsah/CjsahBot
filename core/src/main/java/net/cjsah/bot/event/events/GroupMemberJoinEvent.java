package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.data.enums.ChangeType;
import net.cjsah.bot.data.enums.GroupMemberJoinType;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupMemberJoinEvent extends GroupMemberChangeEvent {
    public static final Codec<GroupMemberJoinEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupMemberJoinEvent::getGroupId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupMemberJoinEvent::getUserId),
        Codec.LONG.fieldOf("operator_id").forGetter(GroupMemberJoinEvent::getOperatorId),
        GroupMemberJoinType.CODEC.fieldOf("sub_type").forGetter(GroupMemberJoinEvent::getJoinType)
    ).apply(instance, GroupMemberJoinEvent::new));

    private final GroupMemberJoinType joinType;

    public GroupMemberJoinEvent(long groupId, long userId, long operatorId, GroupMemberJoinType joinType) {
        super(groupId, userId, operatorId, ChangeType.INCREASE);
        this.joinType = joinType;
    }
}
