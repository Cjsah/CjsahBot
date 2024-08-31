package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.permission.PermissionNodeType;
import net.cjsah.bot.permission.PermissionRoleNode;

public class BlackGroupNode extends PermissionNode {
    public BlackGroupNode(JSONObject json) {
        super(json);
    }

    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof WhiteGroupNode;
    }

    @Override
    public PermissionNodeType getType() {
        return PermissionNodeType.BLACK_GROUP;
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
        return this.isMatch(groupId);
    }

    @Override
    public void handle(PermissionRoleNode node) {

    }
}
