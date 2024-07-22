package net.cjsah.bot.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

public class JsonUtil {

    public static <T> String serialize(T data) {
        return JSON.toJSONString(data);
    }

    public static <T> T deserialize(String str, Class<T> clazz) {
        return JSON.parseObject(str, clazz);
    }

    public static JSONObject deserialize(String str) {
        return JSON.parseObject(str);
    }

    public static <T> T convert(JSONObject json, Class<T> clazz) {
        return json.to(clazz);
    }
}
