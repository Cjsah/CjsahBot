package net.cjsah.bot.api.card;

import com.alibaba.fastjson2.JSONObject;

public abstract class AbstractCardModule {
    private final CardItem parent;
    private final String type;

    protected AbstractCardModule(CardItem parent, String type) {
        this.parent = parent;
        this.type = type;
    }

    public JSONObject generate() {
        return JSONObject.of("type", this.type);
    }

    public CardItem end() {
        return this.parent;
    }
}
