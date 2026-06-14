package net.cjsah.bot.api.message.nodes;

import net.cjsah.bot.api.message.MessageNodeType;

import java.util.Map;

// https://github.com/mamoe/mirai/blob/f5eefae7ecee84d18a66afce3f89b89fe1584b78/mirai-core/src/commonMain/kotlin/net.mamoe.mirai/message/data/HummerMessage.kt#L49
public class PokeMessageNode extends MessageNode {
    private final int pokeType;
    private final int id;
    private final String name;

    public PokeMessageNode(int pokeType, int id) {
        super(MessageNodeType.POKE);
        this.pokeType = pokeType;
        this.id = id;
        this.name = null;
    }

    public PokeMessageNode(JSONObject json) {
        super(MessageNodeType.POKE);
        this.pokeType = this.parseToInt(json, "type");
        this.id = this.parseToInt(json, "id");
        this.name = json.getString("name");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("type", String.valueOf(this.pokeType));
        json.put("id", String.valueOf(this.id));
    }

    @Override
    public String toString() {
        return this.pair("poke", Map.of(
                "type", this.pokeType,
                "id", this.id,
                "name", this.name
        ));
    }

}
