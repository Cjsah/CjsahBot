package net.cjsah.bot.plugin;

import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

public class PluginInfo {
    private final String id;
    private final String name;
    private final String description;
    private final String version;
    private final Map<String, Object> info;

    protected PluginInfo(String id, String name, String description, String version, Map<String, Object> info) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
        this.info = info;
    }

    protected PluginInfo(JSONObject json) {
        this.id = json.getString("id");
        this.name = json.getString("name");
        this.description = json.getString("description");
        this.version = json.getString("version");
        this.info = json.getJSONObject("info");
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getVersion() {
        return this.version;
    }

    public Map<String, Object> getInfo() {
        return this.info;
    }

    @Override
    public String toString() {
        return "PluginInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", info=" + info +
                '}';
    }
}
