package net.cjsah.bot.event.events;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.source.GroupCommandSource;
import net.cjsah.bot.data.GroupUserData;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.util.CodecUtil;
import org.jetbrains.annotations.Nullable;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupMessageEvent extends MessageEvent<GroupUserData> {
    public static final Codec<GroupMessageEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("message_id").forGetter(GroupMessageEvent::getMessageId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupMessageEvent::getUserId),
        CodecUtil.JSON.fieldOf("message").forGetter(GroupMessageEvent::getMessage),
        Codec.STRING.fieldOf("raw_message").forGetter(GroupMessageEvent::getRawMessage),
        GroupUserData.CODEC.fieldOf("sender").forGetter(GroupMessageEvent::getSender),
        Codec.LONG.fieldOf("group_id").forGetter(GroupMessageEvent::getGroupId),
        CodecUtil.JSON.optionalFieldOf("anonymous", null).forGetter(GroupMessageEvent::getAnonymous)
    ).apply(instance, GroupMessageEvent::new));

    private final long groupId;
    @Nullable
    private final JsonElement anonymous;

    public GroupMessageEvent(long messageId, long userId, JsonElement message, String rawMessage, GroupUserData sender, long groupId, @Nullable JsonElement anonymous) {
        super(messageId, userId, message, rawMessage, sender, MessageType.GROUP);
        this.groupId = groupId;
        this.anonymous = anonymous;
    }
    
    @Override
    public CommandSource<?> genCommandSource() {
        return new GroupCommandSource(this);
    }
}