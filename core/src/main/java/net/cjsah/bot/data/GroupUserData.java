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
        GroupRole.CODEC.fieldOf("role").forGetter(GroupUserData::getRole),
        Codec.STRING.fieldOf("title").forGetter(GroupUserData::getTitle),
        Codec.STRING.fieldOf("level").forGetter(GroupUserData::getLevel)
    ).apply(instance, GroupUserData::new));

    private final String card;
    private final GroupRole role;
    private final String title;
    private final String level;

    public GroupUserData(long userId, String nickname, String card, GroupRole role, String title, String level) {
        super(userId, nickname);
        this.card = card;
        this.role = role;
        this.title = title;
        this.level = level;
    }
}
