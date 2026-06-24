package net.cjsah.bot.packet.response.payload;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FriendInfo(
    long userId,
    String nickname,
    String remark
) implements ResponsePacket {

    public static final Codec<FriendInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.fieldOf("user_id").forGetter(FriendInfo::userId),
        Codec.STRING.fieldOf("nickname").forGetter(FriendInfo::nickname),
        Codec.STRING.fieldOf("remark").forGetter(FriendInfo::remark)
    ).apply(instance, FriendInfo::new));

    public String getShowName() {
        return this.remark == null || this.remark.isEmpty() ? this.nickname : this.remark;
    }

}
