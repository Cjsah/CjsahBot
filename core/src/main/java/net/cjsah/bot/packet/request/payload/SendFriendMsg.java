package net.cjsah.bot.packet.request.payload;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.packet.request.RequestType;

public record SendFriendMsg(String userId, MessageChain message) implements RequestPacket {
    public static final Codec<SendFriendMsg> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("user_id").forGetter(SendFriendMsg::userId),
        MessageChain.CODEC.fieldOf("message").forGetter(SendFriendMsg::message)
    ).apply(instance, SendFriendMsg::new));

    public SendFriendMsg(String userId, String message) {
        this(userId, MessageChain.raw(message));
    }

    @Override
    public RequestType getPacketType() {
        return RequestType.SEND_FRIEND_MSG;
    }
}
