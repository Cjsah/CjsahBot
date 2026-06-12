package net.cjsah.bot.exception;

import java.util.function.Function;

public class CustomRuntimeExceptionFactory<T extends RuntimeException> extends CustomExceptionFactory<T> {

    public CustomRuntimeExceptionFactory(String message, Function<String, T> factory) {
        super(message, factory);
    }

    public static  <E extends Exception> CustomRuntimeExceptionFactory<?> runtime(String message, Function<String, E> factory) {
        return new CustomRuntimeExceptionFactory<>(message, msg -> new RuntimeException(factory.apply(msg)));
    }

}
