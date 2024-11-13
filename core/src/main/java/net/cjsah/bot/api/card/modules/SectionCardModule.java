package net.cjsah.bot.api.card.modules;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.CardBuilder;
import net.cjsah.bot.api.card.AbstractCardModule;
import net.cjsah.bot.api.card.CardItem;
import net.cjsah.bot.data.Size;
import net.cjsah.bot.data.TextType;
import net.cjsah.bot.data.Theme;
import net.cjsah.bot.exception.BuiltExceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SectionCardModule extends AbstractCardModule {
    private final List<JSONObject> paragraph;
    private final List<Boolean> types;

    public SectionCardModule(CardItem parent) {
        super(parent, "section");
        this.paragraph = new ArrayList<>(3);
        this.types = new ArrayList<>(3);
    }

    public SectionCardModule text(TextType type, String text) {
        this.append(type.getValue(), true, index -> index != 2 || (this.types.get(0) && this.types.get(1))).put("text", text);
        return this;
    }

    public SectionCardModule image(String url, Size size) {
        JSONObject node = this.append("image", false, index -> index == 0 || (index == 1 && this.types.get(0)));
        node.put("url", url);
        node.put("size", size.getValue());
        return this;
    }

    public SectionCardModule button(String text, String value, boolean isLink, Theme theme) {
        JSONObject node = this.append("button", false, index -> index == 1 && this.types.get(0));
        node.put("event", isLink ? "link-to" : "server");
        node.put("text", text);
        node.put("value", value);
        node.put("theme", theme.getValue());
        return this;
    }

    private JSONObject append(String type, boolean isText, Predicate<Integer> check) {
        CardBuilder.checkMaxSize(this.paragraph, 3);
        check.test(this.paragraph.size());
        JSONObject node = JSONObject.of("type", type);
        this.paragraph.add(node);
        this.types.add(isText);
        return node;
    }

    @Override
    public JSONObject generate() {
        JSONObject result = super.generate();
        result.put("paragraph", this.paragraph);
        return result;
    }

    @Override
    public CardItem end() {
        CardBuilder.checkEmpty(this.paragraph);
        if (this.types.size() == 1 && !this.types.get(0)) {
            throw BuiltExceptions.MSG_UNSUPPORTED_DATA.create();
        }
        return super.end();
    }
}
