package net.cjsah.bot.command.source;

import net.cjsah.bot.permission.PermissionRole;

public abstract class CommandSource<T> {

    protected final T sender;

    public CommandSource(T sender) {
        this.sender = sender;
    }

    public abstract void sendFeedback(String message);

    public boolean hasPermission(PermissionRole role) {
        return true;
    }
}
