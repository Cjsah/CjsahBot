package net.cjsah.bot.exception;

public class RequestException extends RuntimeException {

    public RequestException(String message) {
        super(message, null, true, true);
    }
}
