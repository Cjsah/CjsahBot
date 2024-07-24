package net.cjsah.bot.resolver;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import net.cjsah.bot.msg.MessageChain;

import java.lang.reflect.Type;
import java.util.Collection;

public class MessageDeserializer implements ObjectReader<MessageChain> {

    @Override
    public MessageChain readObject(JSONReader reader, Type type, Object fieldName, long features) {
        if (reader.nextIfNull()) return MessageChain.EMPTY;
        JSONArray array = reader.readJSONArray();
        return MessageChain.parse(array);
    }

    @Override
    public MessageChain createInstance(Collection collection, long features) {
        if (!collection.isEmpty() && collection instanceof JSONArray array) {
            return MessageChain.parse(array);
        }
        return MessageChain.EMPTY;
    }
}
