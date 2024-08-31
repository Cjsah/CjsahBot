package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.permission.PermissionNodeType;
import net.cjsah.bot.permission.PermissionRoleNode;

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

    @Override
    public PermissionNodeType getType() {
        return PermissionNodeType.BLACK_GROUP_USER;
    }

    @Override
    public boolean canUseInGroup() {
        return true;
    }

    @Override
    public boolean canUseInUser() {
        return false;
    }

    @Override
    public boolean match(long groupId, long userId) {
        return this.groupId == groupId && this.isMatch(userId);
    }

    @Override
    public void handle(PermissionRoleNode node) {

    }

}
