package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.permission.PermissionNodeType;
import net.cjsah.bot.permission.PermissionRoleNode;

public class BlackUserNode extends PermissionNode {
    public BlackUserNode(JSONObject json) {
        super(json);
    }

    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof WhiteUserNode;
    }

    @Override
    public PermissionNodeType getType() {
        return PermissionNodeType.BLACK_USER;
    }

    @Override
    public boolean canUseInGroup() {
        return false;
    }

    @Override
    public boolean canUseInUser() {
        return true;
    }

    @Override
    public boolean match(long groupId, long userId) {
        return this.isMatch(userId);
    }

    @Override
    public void handle(PermissionRoleNode node) {

    }

}
