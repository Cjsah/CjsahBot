package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.data.enums.ChangeType;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupAdminChangeEvent extends ReceivedEvent {
    public static final Codec<GroupAdminChangeEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupAdminChangeEvent::getGroupId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupAdminChangeEvent::getUserId),
        Codec.LONG.fieldOf("operator_id").forGetter(GroupAdminChangeEvent::getOperatorId),
        ChangeType.CODEC_SET.fieldOf("sub_type").forGetter(GroupAdminChangeEvent::getType)
    ).apply(instance, GroupAdminChangeEvent::new));

    private final long groupId;
    private final long userId;
    private final long operatorId;
    private final ChangeType type;
}
