package net.cjsah.bot.permission;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.permission.role.BlackChannelNode;
import net.cjsah.bot.permission.role.BlackRoomNode;
import net.cjsah.bot.permission.role.BlackUserNode;
import net.cjsah.bot.permission.role.PermissionNode;
import net.cjsah.bot.permission.role.RoleSetNode;
import net.cjsah.bot.permission.role.WhiteChannelNode;
import net.cjsah.bot.permission.role.WhiteRoomNode;
import net.cjsah.bot.permission.role.WhiteUserNode;

import java.util.Arrays;
import java.util.function.Function;

public enum PermissionNodeType {
    WHITE_ROOM("white_room", WhiteRoomNode::new),
    WHITE_CHANNEL("white_channel", WhiteChannelNode::new),
    WHITE_USER("white_user", WhiteUserNode::new),
    BLACK_ROOM("black_room", BlackRoomNode::new),
    BLACK_CHANNEL("black_channel", BlackChannelNode::new),
    BLACK_USER("black_user", BlackUserNode::new),
    USER_SET("user_set", RoleSetNode::new),
    ;

    PermissionNodeType(String title, Function<JSONObject, PermissionNode> factory) {
        this.title = title;
        this.factory = factory;
    }

    private final String title;
    private final Function<JSONObject, PermissionNode> factory;

    public String getTitle() {
        return this.title;
    }

    public static PermissionNode generate(JSONObject json) {
        String type = json.getString("type");
        PermissionNodeType nodeType = Arrays.stream(values())
                .filter(it -> it.title.equals(type))
                .findFirst()
                .orElseThrow(() -> BuiltExceptions.UNKNOWN_PERMISSION_TYPE.create(type));
        return nodeType.factory.apply(json);
    }
}
