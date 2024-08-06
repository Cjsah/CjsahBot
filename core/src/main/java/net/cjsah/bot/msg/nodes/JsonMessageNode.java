package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;
import net.cjsah.bot.util.JsonUtil;
import net.cjsah.bot.util.StringUtil;

@Getter
public class JsonMessageNode extends MessageNode {
    private final JSONObject json;

    public JsonMessageNode(String json) {
        this(JsonUtil.deserialize(json), false);
    }

    public JsonMessageNode(JSONObject json, boolean next) {
        super(MessageType.JSON);
        if (next) {
            String data = json.getString("data");
            this.json = JsonUtil.deserialize(data);
        } else {
            this.json = json;
        }
    }

    @Override
    public void serializeData(JSONObject json) {
        String str = JsonUtil.serialize(this.json);
        json.put("data", str);
    }

    @Override
    public String toString() {
        return this.toString("json", this.json);
    }
}
