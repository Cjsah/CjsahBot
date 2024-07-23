package net.cjsah.bot.resolver;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import net.cjsah.bot.msg.MessageChain;

import java.lang.reflect.Type;

public class MessageSerializer implements ObjectWriter<MessageChain> {
    @Override
    public void write(JSONWriter writer, Object object, Object fieldName, Type type, long features) {
        MessageChain chain = (MessageChain) object;
        writer.write(chain.toJson());
    }
}
