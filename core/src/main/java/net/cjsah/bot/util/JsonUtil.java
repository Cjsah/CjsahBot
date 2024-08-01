package net.cjsah.bot.util;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import kotlin.text.Charsets;
import net.cjsah.bot.data.enums.MessageSource;
import net.cjsah.bot.msg.MessageChain;
import net.cjsah.bot.resolver.EnumDeserializer;
import net.cjsah.bot.resolver.EnumSerializer;
import net.cjsah.bot.resolver.MessageDeserializer;
import net.cjsah.bot.resolver.MessageSerializer;

import java.io.File;
import java.util.List;

public class JsonUtil {

    public static <T> String serialize(T data) {
        return JSON.toJSONString(data);
    }

    public static <T> T deserialize(String str, Class<T> clazz) {
        return JSON.parseObject(str, clazz, JSONReader.Feature.SupportSmartMatch);
    }

    public static JSONObject deserialize(String str) {
        return JSON.parseObject(str, JSONReader.Feature.SupportSmartMatch);
    }

    public static JSONObject deserializePath(String path) {
        File file = new File(path);
        String str = FileUtil.readString(file, Charsets.UTF_8);
        return JSON.parseObject(str, JSONReader.Feature.SupportSmartMatch);
    }

    public static <T> T getObject(JSONObject json, String key, Class<T> clazz) {
        return json.getObject(key, clazz, JSONReader.Feature.SupportSmartMatch);
    }

    public static <T> List<T> getList(JSONObject json, String key, Class<T> clazz) {
        return json.getList(key, clazz, JSONReader.Feature.SupportSmartMatch);
    }

    public static <T> T convert(JSONObject json, Class<T> clazz) {
        return json.to(clazz);
    }

    public static <T> T convert(Object json, Class<T> clazz) {
        return convert((JSONObject) json, clazz);
    }

    public static <T> List<T> convertList(Object array, Class<T> clazz) {
        return ((JSONArray) array).toList(clazz, JSONReader.Feature.SupportSmartMatch);
    }

    static {
        JSON.register(MessageSource.class, new EnumSerializer<MessageSource>("source"));
        JSON.register(MessageSource.class, new EnumDeserializer<MessageSource>("source"));
        JSON.register(MessageChain.class, new MessageSerializer());
        JSON.register(MessageChain.class, new MessageDeserializer());
    }
}
