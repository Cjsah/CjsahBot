package net.cjsah.bot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtil {
    private static final ObjectMapper ObjectMapper = new ObjectMapper();

    public static <T> String serialize(T data) throws JsonProcessingException {
        return ObjectMapper.writeValueAsString(data);
    }

    public static <T> T deserialize(String str, Class<T> clazz) throws JsonProcessingException {
        return ObjectMapper.readValue(str, clazz);
    }

    public static ObjectNode deserialize(String str) throws JsonProcessingException {
        return ObjectMapper.readValue(str, ObjectNode.class);
    }

    public static <T> T convert(ObjectNode json, Class<T> clazz) {
        return ObjectMapper.convertValue(json, clazz);
    }

    static {
        ObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
