package net.cjsah.bot.packet.request;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.IStrSerializable;
import net.cjsah.bot.packet.request.payload.RequestPacket;
import net.cjsah.bot.packet.request.payload.SendFriendMsg;
import net.cjsah.bot.packet.request.payload.SendGroupMsg;
import net.cjsah.bot.util.CodecUtil;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum RequestType implements IStrSerializable {
    SEND_GROUP_MSG("send_group_msg", SendGroupMsg.CODEC),
    SEND_FRIEND_MSG("send_private_msg", SendFriendMsg.CODEC),
    ;

    public static final Codec<RequestType> CODEC = IStrSerializable.fromEnum(RequestType.class);
    private final String type;
    private final Codec<? extends RequestPacket> codec;

    @Override
    public String getSerializedName() {
        return this.type;
    }

    public static Either<String, String> encode(RequestPayload<?> payload) {
        @SuppressWarnings("unchecked")
        Codec<RequestPayload<? extends RequestPacket>> codec = Inner.CACHED_CODEC.computeIfAbsent(
            payload.getAction(),
            key -> (Codec<RequestPayload<?>>) (Object) RequestPayload.codec(key.codec)
        );
        return CodecUtil.encode(codec, payload);
    }

    private static class Inner {
        private static final Map<RequestType, Codec<RequestPayload<? extends RequestPacket>>> CACHED_CODEC = new HashMap<>();
    }
}
