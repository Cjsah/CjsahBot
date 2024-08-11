package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;

public class WhiteGroupUserNode extends PermissionNode {
    private final long groupId;
    public WhiteGroupUserNode(JSONObject json) {
        super(json);
        this.groupId = json.getLong("group_id");
    }

    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof BlackGroupUserNode;
    }

}
