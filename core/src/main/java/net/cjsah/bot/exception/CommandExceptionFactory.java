package net.cjsah.bot.exception;

public class CommandExceptionFactory extends CustomExceptionFactory<CommandException> {
    public CommandExceptionFactory(String message) {
        super(message, CommandException::new);
    }
}
