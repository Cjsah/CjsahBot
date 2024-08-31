package net.cjsah.bot.exception;

public class PermissionException extends RuntimeException {

    public PermissionException(String message) {
        super(message, null, true, true);
    }
}
