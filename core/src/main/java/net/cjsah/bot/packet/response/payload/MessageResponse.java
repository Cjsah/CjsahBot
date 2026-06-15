package net.cjsah.bot.packet.response.payload;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MessageResponse(long messageId) implements ResponsePacket {
    public static final Codec<MessageResponse> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("message_id").forGetter(MessageResponse::messageId)
    ).apply(instance, MessageResponse::new));
}
