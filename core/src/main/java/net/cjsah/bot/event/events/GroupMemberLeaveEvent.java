package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.data.enums.ChangeType;
import net.cjsah.bot.data.enums.GroupMemberLeaveType;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupMemberLeaveEvent extends GroupMemberChangeEvent {
    public static final Codec<GroupMemberLeaveEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupMemberLeaveEvent::getGroupId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupMemberLeaveEvent::getUserId),
        Codec.LONG.fieldOf("operator_id").forGetter(GroupMemberLeaveEvent::getOperatorId),
        GroupMemberLeaveType.CODEC.fieldOf("sub_type").forGetter(GroupMemberLeaveEvent::getLeaveType)
    ).apply(instance, GroupMemberLeaveEvent::new));

    private final GroupMemberLeaveType leaveType;

    public GroupMemberLeaveEvent(long groupId, long userId, long operatorId, GroupMemberLeaveType leaveType) {
        super(groupId, userId, operatorId, ChangeType.DECREASE);
        this.leaveType = leaveType;
    }
}
