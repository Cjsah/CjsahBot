package net.cjsah.bot.exception;

import java.util.function.Function;

public class CustomExceptionFactory<T extends RuntimeException> {
    private final String message;
    private final Function<String, T> factory;

    public CustomExceptionFactory(String message, Function<String, T> factory) {
        this.message = message;
        this.factory = factory;
    }

    public T create(Object... args) {
        String msg = String.format(this.message, args);
        return this.factory.apply(msg);
    }

}
