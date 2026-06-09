package net.cjsah.bot.config.permission;

import com.mojang.serialization.Codec;
import net.cjsah.bot.util.CodecUtil;

public enum UserRole {
    OWNER("owner", 255),
    ADMIN("admin", 100),
    HELPER("helper", 10),
    USER("user", 5),
    ;

    public static final Codec<UserRole> CODEC = CodecUtil.enumCodec(UserRole.class);
    private final String name;
    private final int level;

    UserRole(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return this.level;
    }
}
