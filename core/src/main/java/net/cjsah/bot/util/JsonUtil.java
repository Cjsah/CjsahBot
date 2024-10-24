package net.cjsah.bot.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;

import java.io.Reader;
import java.util.List;

public final class JsonUtil {

    public static <T> String serialize(T data) {
        return JSON.toJSONString(data);
    }

    public static <T> T deserialize(String str, Class<T> clazz) {
        return JSON.parseObject(str, clazz, JSONReader.Feature.SupportSmartMatch);
    }

    public static JSONObject deserialize(Reader reader) {
        return JSON.parseObject(reader, JSONReader.Feature.SupportSmartMatch);
    }

    public static JSONObject deserialize(String str) {
        return JSON.parseObject(str, JSONReader.Feature.SupportSmartMatch);
    }

    public static <T> T getObject(JSONObject json, String key, Class<T> clazz) {
        return json.getObject(key, clazz, JSONReader.Feature.SupportSmartMatch);
    }

    public static <T> List<T> getList(JSONObject json, String key, Class<T> clazz) {
        return json.getList(key, clazz, JSONReader.Feature.SupportSmartMatch);
    }
}
