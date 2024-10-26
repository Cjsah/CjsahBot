package net.cjsah.bot.permission.role;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.permission.PermissionNodeType;
import net.cjsah.bot.permission.PermissionRoleNode;

public class BlackRoomNode extends PermissionNode {
    public BlackRoomNode(JSONObject json) {
        super(json);
    }

    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof WhiteRoomNode;
    }

    @Override
    public PermissionNodeType getType() {
        return PermissionNodeType.BLACK_ROOM;
    }

    @Override
    public boolean match(String roomId, String channelId, long userId) {
        return this.isMatch(roomId);
    }

    @Override
    public void handle(PermissionRoleNode node) {
        node.deny();
    }

}
