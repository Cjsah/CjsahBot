package net.cjsah.bot.packet.request.payload;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.api.message.MessageChain;
import net.cjsah.bot.packet.request.RequestType;

public record SendGroupMsg(String groupId, MessageChain message) implements RequestPacket {
    public static final Codec<SendGroupMsg> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("group_id").forGetter(SendGroupMsg::groupId),
        MessageChain.CODEC.fieldOf("message").forGetter(SendGroupMsg::message)
    ).apply(instance, SendGroupMsg::new));

    public SendGroupMsg(String groupId, String message) {
        this(groupId, MessageChain.raw(message));
    }

    @Override
    public RequestType getPacketType() {
        return RequestType.SEND_GROUP_MSG;
    }
}
