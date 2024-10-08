package net.cjsah.bot.command;

import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.argument.special.EmptyArgument;
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
    Class<? extends Argument<?>> resolver() default EmptyArgument.class;
}
