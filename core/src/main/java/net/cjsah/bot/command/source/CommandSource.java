package net.cjsah.bot.command.source;

import net.cjsah.bot.permission.HeyboxPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CommandSource<T> {

    protected final T sender;

    public CommandSource(T sender) {
        this.sender = sender;
    }

    private static final Logger log = LoggerFactory.getLogger("Console");

    public boolean hasPermission(HeyboxPermission[] permissions) {
        return true;
//        return PermissionManager.hasPermission(sender.getSenderInfo(), permissions);
    }

    public abstract void sendFeedback(String message);
}
