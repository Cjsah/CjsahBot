package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;

public class BlackGroupNode extends PermissionNode {
    public BlackGroupNode(JSONObject json) {
        super(json);
    }

    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof WhiteGroupNode;
    }
}
