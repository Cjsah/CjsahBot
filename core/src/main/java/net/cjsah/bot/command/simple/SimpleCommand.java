package net.cjsah.bot.command.simple;

import net.cjsah.bot.config.permission.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleCommand {
    String value();

    String description() default "";

    UserRole permission() default UserRole.USER;
}
