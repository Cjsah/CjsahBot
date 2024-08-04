package net.cjsah.bot.exception;

public class Para0CommandException implements CommandExceptionType {
    private final String message;

    public Para0CommandException(String message) {
        this.message = message;
    }

    public CommandException create() {
        return new CommandException(this.message);
    }

    @Override
    public String toString() {
        return this.message;
    }
}
