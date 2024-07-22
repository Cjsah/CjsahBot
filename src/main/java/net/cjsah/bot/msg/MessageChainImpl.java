package net.cjsah.bot.msg;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageChainImpl extends ArrayList<MessageNode> implements MessageChain {
    public MessageChainImpl(MessageNode ...nodes) {
        this.addAll(Arrays.asList(nodes));
    }
}
