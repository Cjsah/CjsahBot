package net.cjsah.bot.exception;

public class EventException extends Exception {
    public EventException(String message) {
        this(message, null);
    }

    public EventException(String message, Throwable cause) {
        super(message, cause, true, true);
    }
}
