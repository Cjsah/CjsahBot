package net.cjsah.bot.api.card.modules;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.card.AbstractCardModule;
import net.cjsah.bot.data.TextType;

public class HeaderCardModule extends AbstractCardModule {
    private final TextType textType;
    private final String text;

    public HeaderCardModule(TextType type, String text) {
        super("header");
        this.textType = type;
        this.text = text;
    }

    @Override
    public JSONObject generate() {
        JSONObject result = super.generate();
        result.put("content", JSONObject.of("type", this.textType.getValue(), "text", this.text));
        return result;
    }
}
