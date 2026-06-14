package net.cjsah.bot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.cjsah.bot.data.enums.GroupRole;

@Getter
@EqualsAndHashCode(callSuper = true)
public class GroupUserData extends BaseUserData {
    public static final Codec<GroupUserData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(GroupUserData::getUserId),
        Codec.STRING.fieldOf("nickname").forGetter(GroupUserData::getNickname),
        Codec.STRING.fieldOf("card").forGetter(GroupUserData::getCard),
        GroupRole.CODEC.fieldOf("role").forGetter(GroupUserData::getRole)
    ).apply(instance, GroupUserData::new));

    private final GroupRole role;

    public GroupUserData(long userId, String nickname, String card, GroupRole role) {
        super(userId, nickname, card);
        this.role = role;
    }
}
