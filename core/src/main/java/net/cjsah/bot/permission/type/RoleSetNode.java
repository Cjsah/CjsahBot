package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;

public class RoleSetNode extends PermissionNode {
    public RoleSetNode(JSONObject json) {
        super(json);
    }

    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof BlackGroupNode;
    }

}
