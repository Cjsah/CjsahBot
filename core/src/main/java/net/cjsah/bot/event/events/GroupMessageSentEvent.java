package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.data.GroupUserData;
import net.cjsah.bot.data.enums.MessageSource;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupMessageSentEvent extends MessageSentEvent<GroupUserData> {
    public static final Codec<GroupMessageSentEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        GroupUserData.CODEC.fieldOf("sender").forGetter(GroupMessageSentEvent::getSender),
        MessageChain.CODEC.fieldOf("message").forGetter(GroupMessageSentEvent::getMessage),
        Codec.LONG.fieldOf("target_id").forGetter(GroupMessageSentEvent::getTargetId),
        Codec.LONG.fieldOf("group_id").forGetter(GroupMessageSentEvent::getGroupId),
        Codec.STRING.fieldOf("group_name").forGetter(GroupMessageSentEvent::getGroupName)
    ).apply(instance, GroupMessageSentEvent::new));

    private final long groupId;
    private final String groupName;

    public GroupMessageSentEvent(GroupUserData sender, MessageChain message, long targetId, long groupId, String groupName) {
        super(MessageSource.FRIEND, targetId, message, sender);
        this.groupId = groupId;
        this.groupName = groupName;
    }
}