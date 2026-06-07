package net.cjsah.bot.config.permission;

import com.mojang.serialization.Codec;
import net.cjsah.bot.util.CodecUtil;

public enum UserRole {
    OWNER("owner", 255),
    ADMIN("admin", 100),
    HELPER("helper", 10),
    USER("user", 5),
    ;

    public static Codec<UserRole> CODEC = CodecUtil.enumCodec(UserRole.class);
    private final String name;
    private final int role;

    UserRole(String name, int role) {
        this.name = name;
        this.role = role;
    }

    public int getRole() {
        return this.role;
    }
}
