package net.cjsah.bot.exception;

public class PluginAdapterException extends PluginException {
    public PluginAdapterException(String message) {
        super(message);
    }
    public PluginAdapterException(Throwable throwable) {
        super(throwable);
    }
    public PluginAdapterException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
