package net.cjsah.bot.plugin.registry;

import lombok.RequiredArgsConstructor;
import net.cjsah.bot.command.CommandDispatcher;
import net.cjsah.bot.command.Commands;
import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.builder.RequiredArgumentBuilder;
import net.cjsah.bot.command.simple.SimpleCommand;
import net.cjsah.bot.command.simple.SimpleCommandParser;
import net.cjsah.bot.config.permission.UserRole;
import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.plugin.PluginMetadata;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Function;

@RequiredArgsConstructor
public class PluginCommandRegistry {
    private final CommandDispatcher dispatcher;
    private final PluginMetadata pluginInfo;

    public LiteralArgumentBuilder literal(String string) {
        return LiteralArgumentBuilder.literal(this.pluginInfo.id(), string);
    }

    public <T> RequiredArgumentBuilder<T> argument(String string, Argument<T> argument) {
        return RequiredArgumentBuilder.argument(this.pluginInfo.id(), string, argument);
    }

    public LiteralArgumentBuilder register(LiteralArgumentBuilder literal) {
        this.dispatcher.register(literal);
        return literal;
    }

    public LiteralArgumentBuilder register(Function<PluginCommandRegistry, LiteralArgumentBuilder> factory) {
        LiteralArgumentBuilder literal = factory.apply(this);
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
        Commands.log.debug("{} registered {} commands", this.pluginInfo.id(), counter);
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
        String desc = annotation.description();
        UserRole permission = annotation.permission();
        SimpleCommandParser parser = new SimpleCommandParser(this, cmd);
        try {
            LiteralArgumentBuilder root = parser.parse(method, desc);
            root.requires(source -> source.hasPermission(permission, root.getPluginIds()));
            this.dispatcher.register(root);
            return true;
        } catch (CommandException e) {
            Commands.log.warn("Failed to parse simple command", e);
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
