package net.cjsah.bot.api.message.nodes;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.message.MessageNodeType;

public class AtMessageNode extends MessageNode {
    private final long qq;

    /**
     * @param qq 要at的qq号, -1为at所有人
     */
    public AtMessageNode(long qq) {
        super(MessageNodeType.AT);
        this.qq = qq;
    }

    public AtMessageNode(JSONObject json) {
        super(MessageNodeType.AT);
        String qq = json.getString("qq");
        this.qq = "all".equals(qq) ? -1 : Long.parseLong(qq);
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("qq", this.qq == -1 ? "all" : String.valueOf(this.qq));
    }

    @Override
    public String toString() {
        return this.toString("at", this.qq);
    }

}
