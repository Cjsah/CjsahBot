package net.cjsah.bot.plugin;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PluginContext {
    protected static final ThreadLocal<Plugin> PLUGIN = new ThreadLocal<>();
    protected static final ThreadLocal<PluginInfo> PLUGIN_INFO = new ThreadLocal<>();

    protected static final Map<String, PluginData> PLUGINS = new ConcurrentHashMap<>();
    protected static final Map<Plugin, PluginData> PLUGIN_MAP = new ConcurrentHashMap<>();

    protected static void appendPlugin(Plugin plugin, PluginInfo info, PluginLoader loader) {
        PluginData data = new PluginData(plugin, info, loader);
        PLUGINS.put(info.getId(), data);
        PLUGIN_MAP.put(plugin, data);
    }

    protected static PluginData removePlugin(Plugin plugin) {
        PluginData data = PluginContext.PLUGIN_MAP.remove(plugin);
        PluginContext.PLUGINS.remove(data.info().getId());
        return data;
    }

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
        PluginData data = PLUGIN_MAP.get(plugin);
        if (data == null) {
            log.warn("Plugin info not found");
            return null;
        }
        return data.info;
    }

    public record PluginData(Plugin plugin, PluginInfo info, PluginLoader loader) {}
}
