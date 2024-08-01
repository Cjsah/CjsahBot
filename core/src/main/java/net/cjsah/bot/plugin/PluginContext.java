package net.cjsah.bot.plugin;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PluginContext {
    protected static final ThreadLocal<Plugin> THREAD_LOCAL = new ThreadLocal<>();

    public static Plugin getCurrentPlugin() {
        return getCurrentPlugin(true);
    }

    public static Plugin getCurrentPlugin(boolean warn) {
        Plugin plugin = THREAD_LOCAL.get();
        if (plugin == null && warn) {
            log.warn("Unknown thread in plugin context");
        }
        return plugin;
    }
}
