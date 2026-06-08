package net.cjsah.bot.exception;

public class CommandException extends Exception {

    public CommandException(String message) {
        super(message, null, true, true);
    }
}
