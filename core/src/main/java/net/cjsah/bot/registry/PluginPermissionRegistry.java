package net.cjsah.bot.registry;

import lombok.RequiredArgsConstructor;
import net.cjsah.bot.command.CommandDispatcher;
import net.cjsah.bot.command.Commands;
import net.cjsah.bot.command.argument.Argument;
import net.cjsah.bot.command.builder.LiteralArgumentBuilder;
import net.cjsah.bot.command.builder.RequiredArgumentBuilder;
import net.cjsah.bot.command.simple.SimpleCommand;
import net.cjsah.bot.command.simple.SimpleCommandParser;
import net.cjsah.bot.config.permission.PermissionPlugin;
import net.cjsah.bot.config.permission.UserRole;
import net.cjsah.bot.exception.CommandException;
import net.cjsah.bot.permission.PermissionManager;
import net.cjsah.bot.plugin.PluginMetadata;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Function;

@RequiredArgsConstructor
public class PluginPermissionRegistry {
    private final PluginMetadata pluginInfo;

    public boolean register(PermissionPlugin permission) {
        return PermissionManager.getInstance().registerPluginPermission(this.pluginInfo.id(), permission);
    }
}
