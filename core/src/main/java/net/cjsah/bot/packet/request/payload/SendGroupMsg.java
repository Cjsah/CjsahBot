package net.cjsah.bot.packet.request.payload;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.packet.request.RequestType;
import net.cjsah.bot.packet.response.payload.MessageResponse;
import net.cjsah.bot.packet.response.payload.ResponsePacket;

public record SendGroupMsg(long groupId, MessageChain message) implements RequestPacket {
    public static final Codec<SendGroupMsg> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(SendGroupMsg::groupId),
        MessageChain.CODEC.fieldOf("message").forGetter(SendGroupMsg::message)
    ).apply(instance, SendGroupMsg::new));

    public SendGroupMsg(long groupId, String message) {
        this(groupId, MessageChain.raw(message));
    }

    @Override
    public RequestType getPacketType() {
        return RequestType.SEND_GROUP_MSG;
    }

    @Override
    public Codec<? extends ResponsePacket> getResponse() {
        return MessageResponse.CODEC;
    }
}
