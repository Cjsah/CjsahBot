package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.permission.PermissionNodeType;
import net.cjsah.bot.permission.PermissionRoleNode;

import java.util.ArrayList;
import java.util.List;

public abstract class PermissionNode {
    private final List<Long> list = new ArrayList<>();

    public PermissionNode(JSONObject json) {
        List<Long> list = json.getList("list", long.class);
        this.list.addAll(list);
    }

    public List<Long> getList() {
        return this.list;
    }

    public boolean isConflict(PermissionNode node) {
        return false;
    }

    protected boolean isMatch(long id) {
        return this.list.contains(id);
    }

    public abstract PermissionNodeType getType();

    public abstract boolean canUseInGroup();
    public abstract boolean canUseInUser();
    public abstract boolean match(long groupId, long userId);

    public abstract void handle(PermissionRoleNode node);

}
