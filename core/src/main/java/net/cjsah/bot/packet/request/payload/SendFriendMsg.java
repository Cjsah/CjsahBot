package net.cjsah.bot.packet.request.payload;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.packet.request.RequestType;
import net.cjsah.bot.packet.response.payload.MessageResponse;
import net.cjsah.bot.packet.response.payload.ResponsePacket;

public record SendFriendMsg(long userId, MessageChain message) implements RequestPacket {
    public static final Codec<SendFriendMsg> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(SendFriendMsg::userId),
        MessageChain.CODEC.fieldOf("message").forGetter(SendFriendMsg::message)
    ).apply(instance, SendFriendMsg::new));

    public SendFriendMsg(long userId, String message) {
        this(userId, MessageChain.raw(message));
    }

    @Override
    public RequestType getPacketType() {
        return RequestType.SEND_FRIEND_MSG;
    }

    @Override
    public Codec<? extends ResponsePacket> getResponse() {
        return MessageResponse.CODEC;
    }
}
