package net.cjsah.bot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.cjsah.bot.data.enums.Sex;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FriendUserData extends BaseUserData {
    public static final Codec<FriendUserData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(FriendUserData::getUserId),
        Codec.STRING.fieldOf("nickname").forGetter(FriendUserData::getNickname),
        Sex.CODEC.fieldOf("sex").forGetter(FriendUserData::getSex),
        Codec.INT.fieldOf("age").forGetter(FriendUserData::getAge)
    ).apply(instance, FriendUserData::new));

    private final Sex sex;
    private final int age;

    public FriendUserData(long userId, String nickname, Sex sex, int age) {
        super(userId, nickname);
        this.sex = sex;
        this.age = age;
    }
}
