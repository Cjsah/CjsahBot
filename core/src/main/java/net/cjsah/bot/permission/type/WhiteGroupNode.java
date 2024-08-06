package net.cjsah.bot.permission.type;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;

public class WhiteGroupNode extends PermissionNode {
    public WhiteGroupNode(JSONObject json, List<Long> list) {
        super(json, list);
    }
}
