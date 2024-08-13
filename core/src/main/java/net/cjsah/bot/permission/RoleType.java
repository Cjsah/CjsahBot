package net.cjsah.bot.permission;

public enum RoleType {
    OWNER("", 255),
    ADMIN("", 100),
    HELPER("", 10),
    USER("", 1);

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
