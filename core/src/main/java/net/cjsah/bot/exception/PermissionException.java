package net.cjsah.bot.exception;

public class PermissionException extends Exception {

    public PermissionException(String message) {
        super(message, null, true, true);
    }
}
