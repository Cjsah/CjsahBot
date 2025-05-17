package net.cjsah.bot.api;

import com.alibaba.fastjson2.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TypedMessage<T> {
    private final int type;
    private final String key;
    private final T content;

    private TypedMessage(int type, String key, T content) {
        this.type = type;
        this.key = key;
        this.content = content;
    }

    public static TypedMessage<String> text(String content) {
        return new TypedMessage<>(0, "content", content);
    }

    public static TypedMessage<JSONObject> markdown(String content) {
        JSONObject markdown = JSONObject.of("content", content);
        return new TypedMessage<>(2, "markdown", markdown);
    }

    public static TypedMessage<JSONObject> markdown(String templateId, Map<String, String> params) {
        List<JSONObject> list = params.entrySet()
                .stream()
                .map(it -> JSONObject.of(it.getKey(), Collections.singletonList(it.getValue())))
                .toList();
        JSONObject markdown = JSONObject.of("custom_template_id", templateId, "params", list);
        return new TypedMessage<>(2, "markdown", markdown);
    }

    public int getType() {
        return this.type;
    }

    public String getKey() {
        return this.key;
    }

    public T getContent() {
        return this.content;
    }
}
