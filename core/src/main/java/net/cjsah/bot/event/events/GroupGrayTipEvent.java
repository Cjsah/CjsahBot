package net.cjsah.bot.event.events;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.util.CodecUtil;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupGrayTipEvent extends ReceivedEvent {
    public static final Codec<GroupGrayTipEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupGrayTipEvent::getGroupId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupGrayTipEvent::getUserId),
        Codec.LONG.fieldOf("message_id").forGetter(GroupGrayTipEvent::getMessageId),
        Codec.STRING.fieldOf("busi_id").forGetter(GroupGrayTipEvent::getBusiId),
        Codec.STRING.fieldOf("content").forGetter(GroupGrayTipEvent::getContent),
        CodecUtil.JSON.optionalFieldOf("raw_info", null).forGetter(GroupGrayTipEvent::getRawInfo)
    ).apply(instance, GroupGrayTipEvent::new));

    private final long groupId;
    private final long userId;
    private final long messageId;
    private final String busiId;
    private final String content;
    private final JsonElement rawInfo;
}
