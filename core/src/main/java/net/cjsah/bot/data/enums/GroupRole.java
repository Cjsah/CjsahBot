package net.cjsah.bot.data.enums;

import com.mojang.serialization.Codec;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.IStrSerializable;

@RequiredArgsConstructor
public enum GroupRole implements IStrSerializable {
    OWNER("owner"),
    ADMIN("admin"),
    MEMBER("member"),
    ;

    public static final Codec<GroupRole> CODEC = IStrSerializable.fromEnum(GroupRole.class);
    private final String role;

    @Override
    public String getSerializedName() {
        return this.role;
    }
}
