package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.msg.MessageNodeType;

// https://github.com/kyubotics/coolq-http-api/wiki/%E8%A1%A8%E6%83%85-CQ-%E7%A0%81-ID-%E8%A1%A8
public class FaceMessageNode extends MessageNode {
    private final int id;

    public FaceMessageNode(int id) {
        super(MessageNodeType.FACE);
        this.id = id;
    }

    public FaceMessageNode(JSONObject json) {
        super(MessageNodeType.FACE);
        this.id = this.parseToInt(json, "id");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("id", String.valueOf(this.id));
    }

    @Override
    public String toString() {
        return this.toString("face", this.id);
    }
}
