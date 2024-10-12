package net.cjsah.bot.command;

import net.cjsah.bot.permission.RoleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String value();

    String description() default "";

    RoleType role() default RoleType.USER;
}
