package net.cjsah.bot.command.source;

import net.cjsah.bot.config.Permissions;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.permission.PermissionRole;
import net.cjsah.bot.permission.context.PermissionContext;

import java.util.function.Function;

public abstract class CommandSource<T> {
    protected final T sender;
    protected final PermissionContext permission;

    public CommandSource(T sender) {
        this.sender = sender;
        this.permission = PermissionManager.getInstance().createContext(this.permissionFactory());
    }

    public abstract void sendFeedback(String message);

    public abstract Function<Permissions, PermissionContext> permissionFactory();

    public abstract PermissionContext createPermissionContext(Permissions permissions, String pluginId);

    public boolean hasPermission(PermissionRole role) {
        return true;
    }
}
