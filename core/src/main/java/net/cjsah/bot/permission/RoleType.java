package net.cjsah.bot.permission;

public enum RoleType {
    OWNER("owner", 255),
    ADMIN("admin", 100),
    HELPER("helper", 10),
    USER("user", 1),
    DENY("deny", 0),
    ;

    RoleType(String name, int level) {
        this.name = name;
        this.level = level;
    }

    private final String name;
    /**
     * 0 - 255
     */
    private final int level;

    public int getLevel() {
        return this.level;
    }

    public static RoleType parse(String name) {
        for (RoleType roleType : values()) {
            if (roleType.name.equals(name)) {
                return roleType;
            }
        }
        return null;
    }
}
