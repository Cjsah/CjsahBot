package net.cjsah.bot.permission.role;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.permission.PermissionNodeType;
import net.cjsah.bot.permission.PermissionRoleNode;

import java.util.List;

public abstract class PermissionNode {
    private final JSONArray list;

    public PermissionNode(JSONObject json) {
        this.list = json.getJSONArray("list");
    }

    public boolean isConflict(PermissionNode node) {
        return false;
    }

    public boolean isConflict(List<PermissionNodeType> types) {
        return false;
    }

    protected <T> boolean isMatch(T id) {
        return this.list.contains(id);
    }

    public abstract PermissionNodeType getType();

    public abstract boolean match(String roomId, String channelId, long userId);

    public abstract void handle(PermissionRoleNode node);

}
