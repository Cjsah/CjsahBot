package net.cjsah.bot.command.source;

public abstract class CommandSource<T> {

    protected final T sender;

    public CommandSource(T sender) {
        this.sender = sender;
    }

    public abstract void sendFeedback(String message);
}
