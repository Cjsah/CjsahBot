package net.cjsah.bot.api.message.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.cjsah.bot.api.message.MessageNodeType;

import java.util.Map;

// https://github.com/mamoe/mirai/blob/f5eefae7ecee84d18a66afce3f89b89fe1584b78/mirai-core/src/commonMain/kotlin/net.mamoe.mirai/message/data/HummerMessage.kt#L49
@Getter
public class PokeMessageNode extends MessageNode {
    public static final Codec<PokeMessageNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("type").forGetter(PokeMessageNode::getPokeType),
        Codec.STRING.fieldOf("id").forGetter(PokeMessageNode::getId)
    ).apply(instance, PokeMessageNode::new));

    private final String pokeType;
    private final String id;

    public PokeMessageNode(String pokeType, String id) {
        super(MessageNodeType.POKE);
        this.pokeType = pokeType;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.pair("poke", Map.of(
            "type", this.pokeType,
            "id", this.id
        ));
    }

}
