package net.cjsah.bot.api.card;

import com.alibaba.fastjson2.JSONObject;

public abstract class AbstractCardModule {
    private final String type;

    protected AbstractCardModule(String type) {
        this.type = type;
    }

    public JSONObject generate() {
        return JSONObject.of("type", this.type);
    }

    public void endCheck() {}
}
