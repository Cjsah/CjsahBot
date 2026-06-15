package net.cjsah.bot.packet.request.payload;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.packet.request.RequestType;
import net.cjsah.bot.packet.response.payload.GroupMember;

public record GetGroupMember(long groupId, long userId) implements RequestPacket {
    public static final Codec<GetGroupMember> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GetGroupMember::groupId),
        Codec.LONG.fieldOf("user_id").forGetter(GetGroupMember::userId)
    ).apply(instance, GetGroupMember::new));

    @Override
    public RequestType getPacketType() {
        return RequestType.GET_GROUP_MEMBER_INFO;
    }

    @Override
    public Codec<?> getResponse() {
        return GroupMember.CODEC;
    }
}
