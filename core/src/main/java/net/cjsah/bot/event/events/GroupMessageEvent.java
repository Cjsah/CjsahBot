package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.source.GroupCommandSource;
import net.cjsah.bot.data.GroupUserData;
import net.cjsah.bot.data.enums.MessageSource;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupMessageEvent extends MessageEvent<GroupUserData> {
    public static final Codec<GroupMessageEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("message_id").forGetter(GroupMessageEvent::getMessageId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupMessageEvent::getUserId),
        MessageChain.CODEC.fieldOf("message").forGetter(GroupMessageEvent::getMessage),
        Codec.STRING.fieldOf("raw_message").forGetter(GroupMessageEvent::getRawMessage),
        GroupUserData.CODEC.fieldOf("sender").forGetter(GroupMessageEvent::getSender),
        Codec.LONG.fieldOf("group_id").forGetter(GroupMessageEvent::getGroupId),
        Codec.STRING.fieldOf("group_name").forGetter(GroupMessageEvent::getGroupName)
    ).apply(instance, GroupMessageEvent::new));

    private final long groupId;
    private final String groupName;

    public GroupMessageEvent(long messageId, long userId, MessageChain message, String rawMessage, GroupUserData sender, long groupId, String groupName) {
        super(messageId, userId, message, rawMessage, sender, MessageSource.GROUP);
        this.groupId = groupId;
        this.groupName = groupName;
    }
    
    @Override
    public CommandSource<?> getCommandSource() {
        return new GroupCommandSource(this);
    }
}