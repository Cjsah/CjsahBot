package net.cjsah.bot.exception;

import java.util.function.Function;

public class Para1CommandException implements CommandExceptionType {
    private final Function<Object, String> function;


    public Para1CommandException(Function<Object, String> function) {
        this.function = function;
    }

    public CommandException create(Object arg) {
        return new CommandException(this.function.apply(arg));
    }

}
