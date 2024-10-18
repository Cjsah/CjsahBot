package net.cjsah.bot.api;

import cn.hutool.core.util.IdUtil;

import java.awt.*;
import java.util.List;

public class RoomRoleBuilder {
    private final String uuid;
    private final String id; // 更新使用
    private final String roomId;
    private final String name;
    private String iconUrl;
    private long permissions;
    private boolean hoist;
    private List<Color> colors;
    private int position; // 更新使用

    public RoomRoleBuilder(String roomId, String name) {
        this(null, roomId, name);
    }

    public RoomRoleBuilder(String id, String roomId, String name) {
        this.uuid = IdUtil.fastSimpleUUID();
        this.id = id;
        this.roomId = roomId;
        this.name = name;
        this.permissions = 0;
        this.hoist = false;
        this.colors = List.of();
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public RoomRoleBuilder setPermissions(long permissions) {
        this.permissions = permissions;
        return this;
    }

    public RoomRoleBuilder addPermission(long permissions) {
        this.permissions |= permissions;
        return this;
    }

    public RoomRoleBuilder setHoist(boolean hoist) {
        this.hoist = hoist;
        return this;
    }

    public RoomRoleBuilder setColor(Color color) {
        this.colors = List.of(color);
        return this;
    }

    public RoomRoleBuilder setColors(List<Color> colors) {
        this.colors = colors;
        return this;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getId() {
        return this.id;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public String getName() {
        return this.name;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public long getPermissions() {
        return this.permissions;
    }

    public boolean isHoist() {
        return this.hoist;
    }

    public List<Color> getColors() {
        return this.colors;
    }

    public int getPosition() {
        return this.position;
    }
}
