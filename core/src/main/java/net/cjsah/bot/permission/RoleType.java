package net.cjsah.bot.permission;

public enum RoleType {
    OWNER("owner", 255),
    ADMIN("admin", 100),
    HELPER("helper", 10),
    USER("user", 1);

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
}
