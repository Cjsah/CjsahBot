package net.cjsah.bot.msg;

import cn.hutool.core.stream.CollectorUtil;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class MessageChainImpl extends ArrayList<MessageNode> implements MessageChain {
    public static final MessageChain EMPTY = new EmptyMessageChain();

    public MessageChainImpl(MessageNode ...nodes) {
        this.addAll(Arrays.asList(nodes));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (MessageNode node : this) {
            builder.append(node.toString());
        }
        return builder.toString();
    }

    public static Collector<MessageNode, MessageChainImpl, MessageChain> list() {
        return new Collector<>() {
            @Override
            public Supplier<MessageChainImpl> supplier() {
                return MessageChainImpl::new;
            }
            @Override
            public BiConsumer<MessageChainImpl, MessageNode> accumulator() {
                return List::add;
            }
            @Override
            public BinaryOperator<MessageChainImpl> combiner() {
                return (left, right) -> { left.addAll(right); return left; };
            }
            @Override
            public Function<MessageChainImpl, MessageChain> finisher() {
                return it -> it;
            }
            @Override
            public Set<Characteristics> characteristics() {
                return CollectorUtil.CH_ID;
            }
        };
    }

    private static class EmptyMessageChain extends AbstractList<MessageNode> implements MessageChain {

        @Override
        public MessageNode get(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return 0;
        }
    }
}
