package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.data.enums.ChangeType;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupMuteEvent extends ReceivedEvent {
    public static final Codec<GroupMuteEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupMuteEvent::getGroupId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupMuteEvent::getUserId),
        Codec.LONG.fieldOf("operator_id").forGetter(GroupMuteEvent::getOperatorId),
        ChangeType.CODEC_MUTE.fieldOf("sub_type").forGetter(GroupMuteEvent::getType),
        Codec.LONG.fieldOf("duration").forGetter(GroupMuteEvent::getDuration)
    ).apply(instance, GroupMuteEvent::new));

    private final long groupId;
    private final long userId;
    private final long operatorId;
    private final ChangeType type;
    private final long duration;

    public GroupMuteEvent(long groupId, long userId, long operatorId, ChangeType type, long duration) {
        this.groupId = groupId;
        this.userId = userId;
        this.operatorId = operatorId;
        this.type = type;
        this.duration = duration;
    }
}
