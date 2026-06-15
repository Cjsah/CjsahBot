package net.cjsah.bot.packet.request.payload;

import com.mojang.serialization.Codec;
import net.cjsah.bot.packet.request.RequestPayload;
import net.cjsah.bot.packet.request.RequestType;
import net.cjsah.bot.packet.response.payload.ResponsePacket;

public interface RequestPacket {
    RequestType getPacketType();

    default RequestPayload<?> packet() {
        RequestType requestType = this.getPacketType();
        return new RequestPayload<>(requestType, this);
    }

    default Codec<? extends ResponsePacket> getResponse() {
        return null;
    }
}
