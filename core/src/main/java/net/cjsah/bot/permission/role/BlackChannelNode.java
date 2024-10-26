package net.cjsah.bot.permission.role;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.permission.PermissionNodeType;
import net.cjsah.bot.permission.PermissionRoleNode;

import java.util.List;

public class BlackChannelNode extends PermissionNode {
    public BlackChannelNode(JSONObject json) {
        super(json);
    }

    @Override
    public boolean isConflict(List<PermissionNodeType> types) {
        return types.contains(PermissionNodeType.WHITE_CHANNEL);
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
