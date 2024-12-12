package net.cjsah.bot.plugin;

import com.alibaba.fastjson2.JSONObject;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public final class PluginInfo {
    private final String id;
    private final String name;
    private final String description;
    private final String version;
    private final Map<String, Object> info;

    PluginInfo(String id, String name, String description, String version, Map<String, Object> info) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
        this.info = info;
    }

    PluginInfo(JSONObject json) {
        this.id = json.getString("id");
        this.name = json.getString("name");
        Object descriptionOrigin = json.get("description");
        if (descriptionOrigin instanceof String str) {
            this.description = str;
        } else if (descriptionOrigin instanceof Collection<?> list) {
            this.description = list.stream().map(Object::toString).collect(Collectors.joining("\n"));
        } else {
            this.description = "";
        }
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
