package net.cjsah.bot.packet.request.payload;

import com.mojang.serialization.Codec;
import net.cjsah.bot.packet.request.RequestType;
import net.cjsah.bot.packet.response.payload.GroupInfo;
import net.cjsah.bot.util.ExtraCodecs;

public record GetGroupList() implements RequestPacket {
    public static final Codec<GetGroupList> CODEC = ExtraCodecs.supplier(GetGroupList::new);

    @Override
    public RequestType getPacketType() {
        return RequestType.GET_GROUP_LIST;
    }

    @Override
    public Codec<?> getResponse() {
        return GroupInfo.CODEC.listOf();
    }
}
