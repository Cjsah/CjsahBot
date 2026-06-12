package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.data.enums.ChangeType;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupEssenceEvent extends ReceivedEvent {
    public static final Codec<GroupEssenceEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupEssenceEvent::getGroupId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupEssenceEvent::getUserId),
        Codec.LONG.fieldOf("message_id").forGetter(GroupEssenceEvent::getMessageId),
        Codec.LONG.fieldOf("sender_id").forGetter(GroupEssenceEvent::getSenderId),
        Codec.LONG.fieldOf("operator_id").forGetter(GroupEssenceEvent::getOperatorId),
        ChangeType.CODEC_CHANGE.fieldOf("sub_type").forGetter(GroupEssenceEvent::getType)
    ).apply(instance, GroupEssenceEvent::new));

    private final long groupId;
    private final long userId;
    private final long messageId;
    private final long senderId;
    private final long operatorId;
    private final ChangeType type;
}
