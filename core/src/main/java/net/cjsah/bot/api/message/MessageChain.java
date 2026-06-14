package net.cjsah.bot.api.message;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.cjsah.bot.api.message.nodes.MessageNode;
import net.cjsah.bot.api.message.nodes.TextMessageNode;

import java.util.Collection;
import java.util.List;

public interface MessageChain extends Collection<MessageNode> {
    Codec<MessageChain> CODEC = Codec.either(
        Codec.STRING,
        MessageNode.NODE_CODEC.listOf()
    ).xmap(either -> either.map(MessageChain::raw, MessageChain::of), chain -> Either.right(chain.stream().toList()));

    MessageChain EMPTY = MessageChainImpl.EMPTY;

    static MessageChain raw(String text) {
        TextMessageNode node = new TextMessageNode(text);
        return new MessageChainImpl(node);
    }

    static MessageChain of(MessageNode... nodes) {
        return new MessageChainImpl(nodes);
    }

    static MessageChain of(List<MessageNode> nodes) {
        return new MessageChainImpl(nodes);
    }
}
