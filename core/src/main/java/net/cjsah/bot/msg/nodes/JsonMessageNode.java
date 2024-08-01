package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;
import net.cjsah.bot.util.JsonUtil;

@Getter
public class JsonMessageNode extends MessageNode {
    private final JSONObject json;

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
        json.put("data", JsonUtil.serialize(this.json));
    }

    @Override
    public String toString() {
        return this.toString("json", this.json);
    }
}
