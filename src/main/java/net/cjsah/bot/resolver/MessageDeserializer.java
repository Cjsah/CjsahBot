package net.cjsah.bot.resolver;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import net.cjsah.bot.msg.MessageChain;

import java.lang.reflect.Type;

public class MessageDeserializer implements ObjectReader<MessageChain> {

    @Override
    public MessageChain readObject(JSONReader reader, Type type, Object fieldName, long features) {
        if (reader.nextIfNull()) return MessageChain.EMPTY;
        JSONArray array = reader.readJSONArray();
        return MessageChain.parse(array);
    }
}
