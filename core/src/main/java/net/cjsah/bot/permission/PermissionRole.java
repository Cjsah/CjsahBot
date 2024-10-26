package net.cjsah.bot.permission;

public enum PermissionRole {
    OWNER("owner", 255),
    ADMIN("admin", 100),
    HELPER("helper", 10),
    USER("user", 5),
    ALL("all", 1),
    DENY("deny", 0),
    ;

    PermissionRole(String name, int level) {
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

    public static PermissionRole parse(String name) {
        for (PermissionRole roleType : values()) {
            if (roleType.name.equals(name)) {
                return roleType;
            }
        }
        return null;
    }
}
