package net.cjsah.bot.exception;

public class MessageException extends RuntimeException {

    public MessageException(String message) {
        super(message, null, true, true);
    }
}
