package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.data.MessageEmojiLike;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupMessageEmojiLikeEvent extends ReceivedEvent {
    public static final Codec<GroupMessageEmojiLikeEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupMessageEmojiLikeEvent::getGroupId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupMessageEmojiLikeEvent::getUserId),
        Codec.LONG.fieldOf("message_id").forGetter(GroupMessageEmojiLikeEvent::getMessageId),
        MessageEmojiLike.CODEC.listOf().fieldOf("likes").forGetter(GroupMessageEmojiLikeEvent::getLikes)
    ).apply(instance, GroupMessageEmojiLikeEvent::new));

    private final long groupId;
    private final long userId;
    private final long messageId;
    private final List<MessageEmojiLike> likes;
}
