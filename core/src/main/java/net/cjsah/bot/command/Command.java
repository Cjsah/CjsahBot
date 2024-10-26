package net.cjsah.bot.command;

import net.cjsah.bot.permission.HeyboxPermission;
import net.cjsah.bot.permission.PermissionRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String value();

    String description() default "";

    HeyboxPermission[] permissions() default HeyboxPermission.USE_BOT_COMMAND;

    PermissionRole role() default PermissionRole.USER;
}
