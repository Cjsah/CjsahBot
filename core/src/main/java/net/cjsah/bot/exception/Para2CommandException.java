package net.cjsah.bot.exception;

import java.util.function.BiFunction;

public class Para2CommandException implements CommandExceptionType {
    private final BiFunction<Object, Object, String> function;


    public Para2CommandException(BiFunction<Object, Object, String> function) {
        this.function = function;
    }

    public CommandException create(Object arg1, Object arg2) {
        return new CommandException(this.function.apply(arg1, arg2));
    }

}
