package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandInfo {
    private final String id;
    private final String command;
    private final Map<String, String> options;

    public CommandInfo(JSONObject json) {
        this.id = json.getString("id");
        this.command = json.getString("name").substring(1);
        List<JSONObject> options = json.getList("options", JSONObject.class);
        this.options = options == null ?
                Collections.emptyMap() :
                options.stream().collect(Collectors.toMap(it -> it.getString("name"), it -> it.getString("value")));
    }

    public String getId() {
        return this.id;
    }

    public String getCommand() {
        return this.command;
    }

    public Map<String, String> getOptions() {
        return this.options;
    }

    @Override
    public String toString() {
        return "CommandInfo{" +
                "id='" + id + '\'' +
                ", command='" + command + '\'' +
                ", options=" + options +
                '}';
    }
}
