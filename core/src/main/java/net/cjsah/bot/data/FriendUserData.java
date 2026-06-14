package net.cjsah.bot.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FriendUserData extends BaseUserData {
    public static final Codec<FriendUserData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(FriendUserData::getUserId),
        Codec.STRING.fieldOf("nickname").forGetter(FriendUserData::getNickname),
        Codec.STRING.fieldOf("card").forGetter(FriendUserData::getCard)
    ).apply(instance, FriendUserData::new));

    public FriendUserData(long userId, String nickname, String card) {
        super(userId, nickname, card);
    }
}
