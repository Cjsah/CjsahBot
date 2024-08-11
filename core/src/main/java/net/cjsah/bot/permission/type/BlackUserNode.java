package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;

public class BlackUserNode extends PermissionNode {
    public BlackUserNode(JSONObject json) {
        super(json);
    }

    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof WhiteUserNode;
    }

}
