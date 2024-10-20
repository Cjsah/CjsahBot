package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.permission.PermissionNodeType;
import net.cjsah.bot.permission.PermissionRoleNode;
import net.cjsah.bot.permission.RoleType;

public class RoleSetNode extends PermissionNode {
    private final RoleType role;
    public RoleSetNode(JSONObject json) {
        super(json);
        this.role = RoleType.parse(json.getString("permission"));
    }

    public RoleType getRole() {
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
