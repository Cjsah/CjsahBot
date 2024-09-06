package net.cjsah.bot.command.source;

import net.cjsah.bot.permission.RoleType;
import net.cjsah.bot.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public abstract class CommandSource<T> {
    protected static final Logger log = LoggerFactory.getLogger("CommandSource");
    protected final T sender;

    public CommandSource(T sender) {
        this.sender = sender;
    }

    public abstract boolean hasPermission(RoleType role);

    public boolean canUse(Plugin plugin) { //TODO permission abstract
        return true;
    }

    public abstract void sendFeedback(String message);

    public abstract void sendFeedback(String message, Level level);

    public T getSender() {
        return this.sender;
    }
}
