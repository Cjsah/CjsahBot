package net.cjsah.bot.command.source;

import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.plugin.Plugin;
import org.slf4j.event.Level;

public abstract class CommandSource<T> {
    protected final T source;

    public CommandSource(T source) {
        this.source = source;
    }

    public boolean hasPermission() { //TODO permission abstract
        return true;
    }

    public boolean canUse(Plugin plugin) { //TODO permission abstract
        return true;
    }

    public abstract void sendFeedback(String message) throws CommandException;

    public abstract void sendFeedback(String message, Level level) throws CommandException;

    public T getSource() {
        return this.source;
    }
}
