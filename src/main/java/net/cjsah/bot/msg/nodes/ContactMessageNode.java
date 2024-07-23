package net.cjsah.bot.msg.nodes;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.ToString;
import net.cjsah.bot.data.enums.MessageSource;
import net.cjsah.bot.data.enums.MessageType;
import net.cjsah.bot.msg.MessageNode;

@Getter
@ToString(callSuper = true)
public class ContactMessageNode extends MessageNode {
    private final MessageSource type;
    private final long id;

    public ContactMessageNode(MessageSource type, int id) {
        super(MessageType.CONTACT);
        this.type = type;
        this.id = id;
    }

    public ContactMessageNode(JSONObject json) {
        super(MessageType.CONTACT);
        String type = json.getString("type");
        this.type = MessageSource.fromName(MessageSource::getContact, type);
        this.id = this.parseToLong(json, "id");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("type", this.type.getContact());
        json.put("id", String.valueOf(this.id));
    }
}
