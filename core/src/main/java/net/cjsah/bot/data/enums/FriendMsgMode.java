package net.cjsah.bot.data.enums;

import com.mojang.serialization.Codec;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.IStrSerializable;

@RequiredArgsConstructor
public enum FriendMsgMode implements IStrSerializable {
    FRIEND("friend", "好友"),
    GROUP("group", "临时"),
    OTHER("other", "其他");

    public static final Codec<FriendMsgMode> CODEC = IStrSerializable.fromEnum(FriendMsgMode.class);

    private final String type;
    @Getter
    private final String text;

    @Override
    public String getSerializedName() {
        return this.type;
    }
}
