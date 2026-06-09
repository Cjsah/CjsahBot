package net.cjsah.bot.permission.context;

import net.cjsah.bot.permission.PermissionRole;

public class ConsolePermissionContext extends PermissionContext {

    public ConsolePermissionContext() {
        super(null, null, 0, null);
        this.level = PermissionRole.OWNER.getLevel();
    }
}
