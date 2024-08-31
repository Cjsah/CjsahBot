package net.cjsah.bot.exception;

public class CommandException extends RuntimeException {

    public CommandException(String message) {
        super(message, null, true, true);
    }
}
