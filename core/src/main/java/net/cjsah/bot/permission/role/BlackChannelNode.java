package net.cjsah.bot.permission.role;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.permission.PermissionNodeType;
import net.cjsah.bot.permission.PermissionRoleNode;

public class BlackChannelNode extends PermissionNode {
    public BlackChannelNode(JSONObject json) {
        super(json);
    }

    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof WhiteChannelNode;
    }

    @Override
    public PermissionNodeType getType() {
        return PermissionNodeType.BLACK_CHANNEL;
    }

    @Override
    public boolean match(String roomId, String channelId, long userId) {
        return this.isMatch(channelId);
    }

    @Override
    public void handle(PermissionRoleNode node) {
        node.deny();
    }
}
