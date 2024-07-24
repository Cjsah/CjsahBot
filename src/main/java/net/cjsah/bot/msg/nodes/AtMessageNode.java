package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

import java.util.Map;

@Getter
public class AtMessageNode extends MessageNode {
    private final long qq;

    /**
     * @param qq 要at的qq号, -1为at所有人
     */
    public AtMessageNode(long qq) {
        super(MessageType.AT);
        this.qq = qq;
    }

    public AtMessageNode(JSONObject json) {
        super(MessageType.AT);
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
