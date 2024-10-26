package net.cjsah.bot.permission.role;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.permission.PermissionNodeType;
import net.cjsah.bot.permission.PermissionRole;
import net.cjsah.bot.permission.PermissionRoleNode;

public class RoleSetNode extends PermissionNode {
    private final PermissionRole role;
    public RoleSetNode(JSONObject json) {
        super(json);
        this.role = PermissionRole.parse(json.getString("permission"));
    }

    public PermissionRole getRole() {
        return this.role;
    }

    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof BlackChannelNode;
    }

    @Override
    public PermissionNodeType getType() {
        return PermissionNodeType.USER_SET;
    }

    @Override
    public boolean match(String roomId, String channelId, long userId) {
        return this.isMatch(userId);
    }

    @Override
    public void handle(PermissionRoleNode node) {
        node.setRole(this.role);
    }

}
