package net.cjsah.bot.permission.context;

import net.cjsah.bot.permission.PermissionRole;

import java.util.Collection;

public class ConsolePermissionContext extends PermissionContext {

    @Override
    public boolean hasPermission(PermissionRole role) {
        return true;
    }

    @Override
    public boolean hasPermission(PermissionRole role, Collection<String> pluginIds) {
        return true;
    }
}
