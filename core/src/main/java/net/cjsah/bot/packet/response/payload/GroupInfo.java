package net.cjsah.bot.packet.response.payload;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record GroupInfo(
    long groupId,
    String groupName,
    int memberCount,
    int maxMemberCount
) implements ResponsePacket {

    public static final Codec<GroupInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupInfo::groupId),
        Codec.STRING.fieldOf("group_name").forGetter(GroupInfo::groupName),
        Codec.INT.fieldOf("member_count").forGetter(GroupInfo::memberCount),
        Codec.INT.fieldOf("max_member_count").forGetter(GroupInfo::maxMemberCount)
    ).apply(instance, GroupInfo::new));

}
