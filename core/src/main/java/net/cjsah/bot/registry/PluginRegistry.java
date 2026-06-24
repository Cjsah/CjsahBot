package net.cjsah.bot.registry;

public interface PluginRegistry {

    PluginCommandRegistry command();

    PluginEventRegistry event();

    PluginPermissionRegistry permission();
}
