package net.cjsah.bot.exception;

public class AppException extends RuntimeException {
    public AppException(String message) {
        this(message, null);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause, true, true);
    }
}
