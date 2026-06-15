package net.cjsah.bot.packet.request.payload;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.packet.request.RequestType;
import net.cjsah.bot.packet.response.payload.GroupMember;

public record GetGroupMembers(long groupId) implements RequestPacket {
    public static final Codec<GetGroupMembers> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GetGroupMembers::groupId)
    ).apply(instance, GetGroupMembers::new));

    @Override
    public RequestType getPacketType() {
        return RequestType.GET_GROUP_MEMBER_LIST;
    }

    @Override
    public Codec<?> getResponse() {
        return GroupMember.CODEC.listOf();
    }
}
