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
    private final MessageSource source;
    private final long id;

    public ContactMessageNode(MessageSource source, int id) {
        super(MessageType.CONTACT);
        this.source = source;
        this.id = id;
    }

    public ContactMessageNode(JSONObject json) {
        super(MessageType.CONTACT);
        String type = json.getString("type");
        this.source = MessageSource.fromName(MessageSource::getContact, type);
        this.id = this.parseToLong(json, "id");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("type", this.source.getContact());
        json.put("id", String.valueOf(this.id));
    }
}
