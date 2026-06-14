package net.cjsah.bot.event.events;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.command.source.FriendCommandSource;
import net.cjsah.bot.data.FriendUserData;
import net.cjsah.bot.data.enums.FriendMsgMode;
import net.cjsah.bot.data.enums.MessageSource;

@Data
@EqualsAndHashCode(callSuper = true)
public class FriendMessageEvent extends MessageEvent<FriendUserData> {
    public static final Codec<FriendMessageEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("message_id").forGetter(FriendMessageEvent::getMessageId),
        Codec.LONG.fieldOf("user_id").forGetter(FriendMessageEvent::getUserId),
        MessageChain.CODEC.fieldOf("message").forGetter(FriendMessageEvent::getMessage),
        Codec.STRING.fieldOf("raw_message").forGetter(FriendMessageEvent::getRawMessage),
        FriendUserData.CODEC.fieldOf("sender").forGetter(FriendMessageEvent::getSender),
        FriendMsgMode.CODEC.fieldOf("sub_type").forGetter(FriendMessageEvent::getMode)
    ).apply(instance, FriendMessageEvent::new));

    private final FriendMsgMode mode;

    public FriendMessageEvent(long messageId, long userId, MessageChain message, String rawMessage, FriendUserData sender, FriendMsgMode mode) {
        super(messageId, userId, message, rawMessage, sender, MessageSource.FRIEND);
        this.mode = mode;
    }

    @Override
    public CommandSource<?> getCommandSource() {
        return new FriendCommandSource(this);
    }
}