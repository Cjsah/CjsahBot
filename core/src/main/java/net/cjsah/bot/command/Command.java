package net.cjsah.bot.command;

import net.cjsah.bot.permission.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String value();

    String description() default "";

    Permission[] permissions() default Permission.USE_BOT_COMMAND;
}
