package net.cjsah.bot.data.enums;

import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.IStrSerializable;

@RequiredArgsConstructor
public enum GroupMemberJoinType implements IStrSerializable {
    APPROVE("approve"),
    INVITE("invite"),
    ;

    public static final Codec<GroupMemberJoinType> CODEC = IStrSerializable.fromEnum(GroupMemberJoinType.class);
    private final String type;

    @Override
    public String getSerializedName() {
        return this.type;
    }
}
