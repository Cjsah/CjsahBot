package net.cjsah.bot.data;

import lombok.Data;
import net.cjsah.bot.data.enums.MessageSource;
import net.cjsah.bot.msg.MessageChain;

@Data
public class Message {
    private final int time;
    private final MessageSource messageType;
    private final int messageId;
    private final int realId;
    private final int sender;
    private final MessageChain message;
}
