package net.cjsah.bot.api;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.exception.BuiltExceptions;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TypedMessage {
    private final int type;
    private final JSONObject content;

    private TypedMessage(int type, JSONObject content) {
        this.type = type;
        this.content = content;
    }

    public TypedMessage keyboard(String id) {
        if (this.type != 2) throw BuiltExceptions.MESSAGE_NOT_MARKDOWN.create();
        this.content.put("keyboard", JSONObject.of("id", id));
        return this;
    }

    public static TypedMessage text(String content) {
        return new TypedMessage(0, JSONObject.of("content", content));
    }

    public static TypedMessage markdown(String content) {
        JSONObject data = JSONObject.of("markdown", JSONObject.of("content", content));
        return new TypedMessage(2, data);
    }

    public static TypedMessage markdown(String templateId, Map<String, String> params) {
        List<JSONObject> list = params.entrySet()
                .stream()
                .map(it -> JSONObject.of(it.getKey(), Collections.singletonList(it.getValue())))
                .toList();
        JSONObject markdown = JSONObject.of("custom_template_id", templateId, "params", list);
        return new TypedMessage(2, JSONObject.of("markdown", markdown));
    }

    public int getType() {
        return this.type;
    }

    public JSONObject getContent() {
        return this.content;
    }
}
