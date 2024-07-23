package net.cjsah.bot.msg;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;

public class MessageChainImpl extends ArrayList<MessageNode> implements MessageChain {
    public static final MessageChain EMPTY = new EmptyMessageChain();

    public MessageChainImpl(MessageNode ...nodes) {
        this.addAll(Arrays.asList(nodes));
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
