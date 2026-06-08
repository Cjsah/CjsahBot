package net.cjsah.bot.command.simple;

import net.cjsah.bot.command.argument.Argument;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParam {
    @NotNull
    String value() default "";

    @NotNull
    String description() default "";
    Class<? extends Argument<?>> resolver();
}
