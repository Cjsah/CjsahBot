package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;

public class BlackGroupUserNode extends PermissionNode {
    private final long groupId;

    public BlackGroupUserNode(JSONObject json) {
        super(json);
        this.groupId = json.getLong("group_id");
    }


    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof WhiteGroupUserNode;
    }

}
