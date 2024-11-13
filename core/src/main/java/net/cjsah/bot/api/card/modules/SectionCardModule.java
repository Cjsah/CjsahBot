package net.cjsah.bot.api.card.modules;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.CardBuilder;
import net.cjsah.bot.api.card.AbstractCardModule;
import net.cjsah.bot.api.card.CardItem;
import net.cjsah.bot.exception.BuiltExceptions;

import java.util.ArrayList;
import java.util.List;

public class SectionCardModule extends AbstractCardModule {
    private final List<JSONObject> paragraph;

    public SectionCardModule(CardItem parent) {
        super(parent, "section");
        this.paragraph = new ArrayList<>(3);
    }

    public SectionCardModule text(String text) {
        this.append("plain-text", text);
        return this;
    }

    public SectionCardModule md(String md) {
        this.append("markdown", md);
        return this;
    }

    private void append(String type, String value) {
        CardBuilder.checkMaxSize(this.paragraph, 3);
        this.paragraph.add(JSONObject.of("type", type, "text", value));
    }

    @Override
    public CardItem end() {
        CardBuilder.checkEmpty(this.paragraph);
        return super.end();
    }
}
