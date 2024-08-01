package net.cjsah.bot.plugin;

import com.alibaba.fastjson2.JSONObject;
import lombok.ToString;

import java.util.Map;

@ToString
public class PluginInfo {
    private final String id;
    private final String name;
    private final String description;
    private final String version;
    private final Map<String, Object> info;

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
}