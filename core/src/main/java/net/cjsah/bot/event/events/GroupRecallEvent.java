package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupRecallEvent extends MessageRecallEvent {
    public static final Codec<GroupRecallEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(GroupRecallEvent::getUserId),
        Codec.LONG.fieldOf("message_id").forGetter(GroupRecallEvent::getMessageId),
        Codec.LONG.fieldOf("group_id").forGetter(GroupRecallEvent::getGroupId),
        Codec.LONG.fieldOf("operator_id").forGetter(GroupRecallEvent::getOperatorId)
    ).apply(instance, GroupRecallEvent::new));

    private final long groupId;
    private final long operatorId;

    public GroupRecallEvent(long userId, long messageId, long groupId, long operatorId) {
        super(userId, messageId);
        this.groupId = groupId;
        this.operatorId = operatorId;
    }
}
