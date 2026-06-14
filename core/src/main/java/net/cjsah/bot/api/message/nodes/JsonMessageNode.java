package net.cjsah.bot.api.message.nodes;

import com.google.gson.JsonElement;
import net.cjsah.bot.api.message.MessageNodeType;
import net.cjsah.bot.util.CodecUtil;

public class JsonMessageNode extends MessageNode {
    private final JsonElement json;

    public JsonMessageNode(String json) {
        this(CodecUtil.decode(CodecUtil.JSON, json).orThrow(), false);
    }

    public JsonMessageNode(JsonElement json, boolean next) {
        super(MessageNodeType.JSON);
        if (next && json.isJsonObject()) {
            String data = json.getAsJsonObject().get("data").getAsString();
            this.json = CodecUtil.decode(CodecUtil.JSON, data).orThrow();
        } else {
            this.json = json;
        }
    }

    @Override
    public void serializeData(JSONObject json) {
        String data = CodecUtil.encode(CodecUtil.JSON, this.json).orThrow();
        json.put("data", data);
    }

    @Override
    public String toString() {
        return this.toString("json", this.json);
    }
}
