package net.cjsah.bot.api.message.nodes;

import net.cjsah.bot.api.message.MessageNodeType;
import net.cjsah.bot.data.enums.MessageSourceDep;

import java.util.Map;

public class ContactMessageNode extends MessageNode {
    private final MessageSourceDep source;
    private final long id;

    public ContactMessageNode(MessageSourceDep source, int id) {
        super(MessageNodeType.CONTACT);
        this.source = source;
        this.id = id;
    }

    public ContactMessageNode(JSONObject json) {
        super(MessageNodeType.CONTACT);
        String type = json.getString("type");
        this.source = MessageSourceDep.fromName(MessageSourceDep::getContact, type);
        this.id = this.parseToLong(json, "id");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("type", this.source.getContact());
        json.put("id", String.valueOf(this.id));
    }

    @Override
    public String toString() {
        return this.toString("contact", Map.of(
                "source", this.source.name().toLowerCase(),
                "id", this.id
        ));
    }
}
