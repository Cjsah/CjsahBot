package net.cjsah.bot.permission;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.permission.type.BlackGroupNode;
import net.cjsah.bot.permission.type.BlackUserNode;
import net.cjsah.bot.permission.type.PermissionNode;
import net.cjsah.bot.permission.type.RoleSetNode;
import net.cjsah.bot.permission.type.WhiteGroupNode;
import net.cjsah.bot.permission.type.WhiteUserNode;

import java.util.Arrays;
import java.util.function.Function;

public enum PermissionNodeType {
    WHITE_GROUP("white_group", WhiteGroupNode::new),
    WHITE_USER("white_user", WhiteUserNode::new),
    BLACK_GROUP("black_group", BlackGroupNode::new),
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
                .orElseThrow(() -> BuiltExceptions.UNKNOWN_PERMISSION_TYPE.apply(type));
        return nodeType.factory.apply(json);
    }
}
