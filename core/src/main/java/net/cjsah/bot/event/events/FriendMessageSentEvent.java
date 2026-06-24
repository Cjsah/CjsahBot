package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.data.FriendUserData;
import net.cjsah.bot.data.enums.FriendMsgMode;
import net.cjsah.bot.data.enums.MessageSource;

@Data
@EqualsAndHashCode(callSuper = true)
public class FriendMessageSentEvent extends MessageSentEvent<FriendUserData> {
    public static final Codec<FriendMessageSentEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        FriendUserData.CODEC.fieldOf("sender").forGetter(FriendMessageSentEvent::getSender),
        MessageChain.CODEC.fieldOf("message").forGetter(FriendMessageSentEvent::getMessage),
        Codec.LONG.fieldOf("target_id").forGetter(FriendMessageSentEvent::getTargetId),
        FriendMsgMode.CODEC.fieldOf("sub_type").forGetter(FriendMessageSentEvent::getMode)
    ).apply(instance, FriendMessageSentEvent::new));

    private final FriendMsgMode mode;

    public FriendMessageSentEvent(FriendUserData sender, MessageChain message, long targetId, FriendMsgMode mode) {
        super(MessageSource.FRIEND, targetId, message, sender);
        this.mode = mode;
    }
}