package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

// https://github.com/mamoe/mirai/blob/f5eefae7ecee84d18a66afce3f89b89fe1584b78/mirai-core/src/commonMain/kotlin/net.mamoe.mirai/message/data/HummerMessage.kt#L49
@Getter
@ToString(callSuper = true)
public class PokeMessageNode extends MessageNode {
    private final int type;
    private final int id;
    private final String name;

    public PokeMessageNode(int type, int id) {
        super(MessageType.POKE);
        this.type = type;
        this.id = id;
        this.name = null;
    }

    public PokeMessageNode(JSONObject json) {
        super(MessageType.POKE);
        this.type = this.parseToInt(json, "type");
        this.id = this.parseToInt(json, "id");
        this.name = json.getString("name");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("type", String.valueOf(this.type));
        json.put("id", String.valueOf(this.id));
    }
}
