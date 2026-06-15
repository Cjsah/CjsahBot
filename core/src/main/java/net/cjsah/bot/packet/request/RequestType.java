package net.cjsah.bot.packet.request;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.cjsah.bot.data.IStrSerializable;
import net.cjsah.bot.packet.request.payload.RequestPacket;
import net.cjsah.bot.packet.request.payload.SendFriendMsg;
import net.cjsah.bot.packet.request.payload.SendGroupMsg;
import net.cjsah.bot.util.CodecUtil;

public enum RequestType implements IStrSerializable {
    SEND_GROUP_MSG("send_group_msg", SendGroupMsg.CODEC),
    SEND_FRIEND_MSG("send_group_msg", SendFriendMsg.CODEC),
    ;

    public static final Codec<RequestType> CODEC = IStrSerializable.fromEnum(RequestType.class);
    private final String type;
    private final Codec<RequestPayload<? extends RequestPacket>> codec;

    @SuppressWarnings("unchecked")
    RequestType(String type, Codec<? extends RequestPacket> codec) {
        this.type = type;
        this.codec = (Codec<RequestPayload<?>>) (Object) RequestPayload.codec(codec);
    }

    @Override
    public String getSerializedName() {
        return type;
    }

    public static Either<String, String> encode(RequestPayload<?> payload) {
        return CodecUtil.encode(payload.getAction().codec, payload);
    }
}
