package net.cjsah.bot.plugin;

import java.util.Map;

public class PluginInfo {
    private final String id;
    private final String name;
    private final String description;
    private final String version;
    private final Map<String, Object> info;

    public PluginInfo(String id, String name, String description, String version, Map<String, Object> info) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
        this.info = info;
    }
}
