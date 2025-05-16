package net.cjsah.bot.exception;

public class PluginException extends RuntimeException {

    public PluginException(String message) {
        super(message, null, true, true);
    }
}
