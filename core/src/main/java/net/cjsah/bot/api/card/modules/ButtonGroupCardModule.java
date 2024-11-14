package net.cjsah.bot.api.card.modules;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.CardBuilder;
import net.cjsah.bot.api.card.AbstractCardModule;
import net.cjsah.bot.data.Theme;

import java.util.ArrayList;
import java.util.List;

public class ButtonGroupCardModule extends AbstractCardModule {
    private final List<JSONObject> buttons;

    public ButtonGroupCardModule() {
        super("button-group");
        this.buttons = new ArrayList<>(3);
    }

    public ButtonGroupCardModule button(String text, String value, boolean isLink, Theme theme) {
        CardBuilder.checkMaxSize(this.buttons, 3);
        JSONObject node = JSONObject.of("type", "button");
        node.put("event", isLink ? "link-to" : "server");
        node.put("text", text);
        node.put("value", value);
        node.put("theme", theme.getValue());
        this.buttons.add(node);
        return this;
    }

    @Override
    public JSONObject generate() {
        JSONObject result = super.generate();
        result.put("btns", this.buttons);
        return result;
    }

    @Override
    public void endCheck() {
        CardBuilder.checkEmpty(this.buttons);
    }
}
