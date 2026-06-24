package net.cjsah.bot.packet.request.payload;

import com.mojang.serialization.Codec;
import net.cjsah.bot.packet.request.RequestType;
import net.cjsah.bot.packet.response.payload.FriendInfo;
import net.cjsah.bot.packet.response.payload.GroupInfo;
import net.cjsah.bot.util.ExtraCodecs;

public record GetFriendList() implements RequestPacket {
    public static final Codec<GetFriendList> CODEC = ExtraCodecs.supplier(GetFriendList::new);

    @Override
    public RequestType getPacketType() {
        return RequestType.GET_FRIEND_LIST;
    }

    @Override
    public Codec<?> getResponse() {
        return FriendInfo.CODEC.listOf();
    }
}
