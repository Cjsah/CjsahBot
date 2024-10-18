package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;

public class ChannelInfo {
    private final String id;
    private final String name;
    private final ChannelType type;

    public ChannelInfo(JSONObject json) {
        this.id = json.getString("channel_id");
        this.name = json.getString("channel_name");
        this.type = ChannelType.of(json.getIntValue("channel_type"));
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ChannelType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "ChannelInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
