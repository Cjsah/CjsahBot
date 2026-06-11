package net.cjsah.bot.command;

import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.builder.RequiredArgumentBuilder;
import net.cjsah.bot.command.simple.SimpleCommand;
import net.cjsah.bot.command.simple.SimpleCommandParser;
import net.cjsah.bot.config.permission.UserRole;
import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.plugin.PluginInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Slf4j(topic = "CommandManager")
public final class CommandRegisterContext {
    private final CommandDispatcher dispatcher;
    private final PluginInfo info;

    public CommandRegisterContext(CommandDispatcher dispatcher, PluginInfo info) {
        this.dispatcher = dispatcher;
        this.info = info;
    }

    public LiteralArgumentBuilder literal(String string) {
        return LiteralArgumentBuilder.literal(this.info.getId(), string);
    }

    public <T> RequiredArgumentBuilder<T> argument(String string, Argument<T> argument) {
        return RequiredArgumentBuilder.argument(this.info.getId(), string, argument);
    }

    public LiteralArgumentBuilder register(LiteralArgumentBuilder literal) {
        this.dispatcher.register(literal);
        return literal;
    }

    public int register(Class<?> clazz) {
        int counter = 0;
        for (Method method : clazz.getDeclaredMethods()) {
            if (this.register(method, true)) {
                counter++;
            }
        }
        log.debug("{} registered {} commands", this.info.getId(), counter);
        return counter;
    }

    public boolean register(Method method) {
        return register(method, false);
    }

    public boolean register(Method method, boolean ignoreValidErr) {
        if (!isValidMethod(method)) {
            if (ignoreValidErr) return false;
            throw new UnsupportedOperationException("The command method not a valid method!");
        }
        SimpleCommand annotation = method.getDeclaredAnnotation(SimpleCommand.class);
        String cmd = annotation.value();
        UserRole permission = annotation.permission();
        SimpleCommandParser parser = new SimpleCommandParser(this, cmd);
        try {
            LiteralArgumentBuilder root = parser.parse(method);
            root.requires(source -> source.hasPermission(permission, root.getPluginIds()));
            this.dispatcher.register(root);
            return true;
        } catch (CommandException e) {
            log.warn("Failed to parse simple command", e);
            return false;
        }
    }

    public boolean isValidMethod(Method method) {
        return Modifier.isPublic(method.getModifiers()) &&
            Modifier.isStatic(method.getModifiers()) &&
            !method.isBridge() &&
            method.isAnnotationPresent(SimpleCommand.class);
    }

}
