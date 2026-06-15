package net.cjsah.bot.packet.response.payload;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.bot.data.enums.GroupRole;
import net.cjsah.bot.data.enums.Sex;
import net.cjsah.bot.util.CodecUtil;

import java.time.Instant;

public record GroupMember(
    long groupId,
    long userId,
    String nickname,
    String card,
    Sex sex,
    int age,
    String level,
    int qqLevel,
    Instant joinTime,
    Instant lastSentTime,
    boolean unfriendly,
    boolean cardChangeable,
    boolean isRobot,
    GroupRole role,
    String title
) implements ResponsePacket {

    public static final Codec<GroupMember> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("group_id").forGetter(GroupMember::groupId),
        Codec.LONG.fieldOf("user_id").forGetter(GroupMember::userId),
        Codec.STRING.fieldOf("nickname").forGetter(GroupMember::nickname),
        Codec.STRING.fieldOf("card").forGetter(GroupMember::card),
        Sex.CODEC.fieldOf("sex").forGetter(GroupMember::sex),
        Codec.INT.fieldOf("age").forGetter(GroupMember::age),
        Codec.STRING.fieldOf("level").forGetter(GroupMember::level),
        Codec.INT.fieldOf("qq_level").forGetter(GroupMember::qqLevel),
        CodecUtil.TIMESTAMP.fieldOf("join_time").forGetter(GroupMember::joinTime),
        CodecUtil.TIMESTAMP.fieldOf("last_sent_time").forGetter(GroupMember::lastSentTime),
        Codec.BOOL.fieldOf("unfriendly").forGetter(GroupMember::unfriendly),
        Codec.BOOL.fieldOf("card_changeable").forGetter(GroupMember::cardChangeable),
        Codec.BOOL.fieldOf("is_robot").forGetter(GroupMember::isRobot),
        GroupRole.CODEC.fieldOf("role").forGetter(GroupMember::role),
        Codec.STRING.fieldOf("title").forGetter(GroupMember::title)
    ).apply(instance, GroupMember::new));

}
