package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;

public class RoomInfo {
    private final String id;
    private final String name;
    private final String avatarUrl;

    public RoomInfo(JSONObject json) {
        this.id = json.getString("room_id");
        this.name = json.getString("room_name");
        this.avatarUrl = json.getString("room_avatar");
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    @Override
    public String toString() {
        return "RoomInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
