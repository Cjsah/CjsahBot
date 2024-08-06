package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PermissionNode {
    private final List<Long> list = new ArrayList<>();

    public PermissionNode(JSONObject json, List<Long> list) {
        this.list.addAll(list);
    }

    public List<Long> getList() {
        return this.list;
    }

    public boolean isConflict(PermissionNode node) {
        return false;
    }
}
