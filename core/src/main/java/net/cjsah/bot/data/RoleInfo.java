package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;

import java.awt.*;
import java.util.List;

public class RoleInfo {
    private final String id;
    private final String name;
    private final String roomId;
    private final long permissions;
    private final String iconUrl;
    private final RoleType type;
    private final Color color;
    private final List<Color> colorList;
    private final int position;
    private final boolean hoist;
    private final String creator;
    private final long createTime;


    public RoleInfo(JSONObject json) {
        this.id = json.getString("id");
        this.name = json.getString("name");
        this.roomId = json.getString("room_id");
        this.permissions = json.getLongValue("permissions");
        this.iconUrl = json.getString("icon");
        this.type = RoleType.of(json.getIntValue("type"));
        this.color = new Color(json.getIntValue("color"));
        List<Integer> colors = json.getList("color_list", int.class);
        this.colorList = colors == null ? List.of() : colors.stream().map(Color::new).toList();
        this.position = json.getIntValue("position");
        this.hoist = json.getBooleanValue("hoist");
        this.creator = json.getString("creator");
        this.createTime = json.getLongValue("create_time");
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public long getPermissions() {
        return this.permissions;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public RoleType getType() {
        return this.type;
    }

    public Color getColor() {
        return this.color;
    }

    public List<Color> getColorList() {
        return this.colorList;
    }

    public int getPosition() {
        return this.position;
    }

    public boolean isHoist() {
        return this.hoist;
    }

    public String getCreator() {
        return this.creator;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    @Override
    public String toString() {
        return "RoomRole{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", roomId='" + roomId + '\'' +
                ", permissions=" + permissions +
                ", iconUrl='" + iconUrl + '\'' +
                ", type=" + type +
                ", color=" + color +
                ", colorList=" + colorList +
                ", position=" + position +
                ", hoist=" + hoist +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
