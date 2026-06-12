package net.cjsah.bot.data.enums;

import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.IStrSerializable;

@RequiredArgsConstructor
public enum GroupMemberLeaveType implements IStrSerializable {
    LEAVE("leave"),     // 退群
    KICK("kick"),       // 踢出
    KICK_ME("kick_me"), // 被踢
    DISBAND("disband"), // 解散
    ;

    public static final Codec<GroupMemberLeaveType> CODEC = IStrSerializable.fromEnum(GroupMemberLeaveType.class);
    private final String type;

    @Override
    public String getSerializedName() {
        return this.type;
    }
}
