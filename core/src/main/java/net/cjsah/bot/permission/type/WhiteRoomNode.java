package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.permission.PermissionNodeType;
import net.cjsah.bot.permission.PermissionRoleNode;

public class WhiteRoomNode extends PermissionNode {
    public WhiteRoomNode(JSONObject json) {
        super(json);
    }

    @Override
    public boolean isConflict(PermissionNode node) {
        return node instanceof BlackRoomNode;
    }

    @Override
    public PermissionNodeType getType() {
        return PermissionNodeType.WHITE_ROOM;
    }

    @Override
    public boolean match(String roomId, String channelId, long userId) {
        return this.isMatch(roomId);
    }

    @Override
    public void handle(PermissionRoleNode node) {
        node.allow();
    }

}
