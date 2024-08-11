package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;

public class WhiteGroupNode extends PermissionNode {
    public WhiteGroupNode(JSONObject json) {
        super(json);
    }

    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof BlackGroupNode;
    }

}
