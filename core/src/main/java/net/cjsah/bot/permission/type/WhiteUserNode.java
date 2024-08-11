package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;

public class WhiteUserNode extends PermissionNode {
    public WhiteUserNode(JSONObject json) {
        super(json);
    }

    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof BlackUserNode;
    }

}
