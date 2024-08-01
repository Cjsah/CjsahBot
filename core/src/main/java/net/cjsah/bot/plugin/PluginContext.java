package net.cjsah.bot.plugin;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PluginContext {
    protected static final ThreadLocal<Plugin> PLUGIN = new ThreadLocal<>();
    protected static final ThreadLocal<PluginInfo> PLUGIN_INFO = new ThreadLocal<>();

    protected static final Map<String, Plugin> PLUGINS = new ConcurrentHashMap<>();
    protected static final Map<Plugin, PluginInfo> PLUGIN_INFO_MAP = new ConcurrentHashMap<>();

    public static Plugin getCurrentPlugin() {
        return getCurrentPlugin(true);
    }

    public static Plugin getCurrentPlugin(boolean warn) {
        Plugin plugin = PLUGIN.get();
        if (plugin == null && warn) {
            log.warn("Unknown thread in plugin context");
        }
        return plugin;
    }

    public static PluginInfo getCurrentPluginInfo() {
        PluginInfo info = PLUGIN_INFO.get();
        if (info == null) {
            log.warn("Unknown thread in plugin context");
        }
        return info;
    }

    public static PluginInfo getPluginInfo(Plugin plugin) {
        PluginInfo info = PLUGIN_INFO_MAP.get(plugin);
        if (info == null) {
            log.warn("Plugin info not found");
        }
        return info;
    }
}
