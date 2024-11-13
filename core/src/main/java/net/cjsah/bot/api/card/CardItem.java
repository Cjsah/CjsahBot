package net.cjsah.bot.api.card;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.CardBuilder;
import net.cjsah.bot.api.card.modules.ButtonGroupCardModule;
import net.cjsah.bot.api.card.modules.CountdownCardModule;
import net.cjsah.bot.api.card.modules.DividerCardModule;
import net.cjsah.bot.api.card.modules.HeaderCardModule;
import net.cjsah.bot.api.card.modules.ImagesCardModule;
import net.cjsah.bot.api.card.modules.SectionCardModule;
import net.cjsah.bot.data.CountdownMode;
import net.cjsah.bot.data.TextType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CardItem {
    private final List<AbstractCardModule> modules;
    private final CardBuilder parent;

    public CardItem(CardBuilder parent) {
        this.parent = parent;
        this.modules = new ArrayList<>(10);
    }

    public JSONObject generate() {
        CardBuilder.checkEmpty(this.modules);
        List<JSONObject> modules = this.modules.stream().parallel().map(AbstractCardModule::generate).toList();
        return JSONObject.of("type", "card", "modules", modules);
    }

    public SectionCardModule section() {
        CardBuilder.checkMaxSize(this.modules, 10);
        SectionCardModule module = new SectionCardModule(this);
        this.modules.add(module);
        return module;
    }

    public CardItem header(TextType type, String text) {
        CardBuilder.checkMaxSize(this.modules, 10);
        HeaderCardModule module = new HeaderCardModule(this, type, text);
        this.modules.add(module);
        return this;
    }

    public ImagesCardModule images(String... urls) {
        CardBuilder.checkMaxSize(this.modules, 10);
        CardBuilder.checkMaxSize(urls, 18);
        ImagesCardModule module = new ImagesCardModule(this, urls);
        this.modules.add(module);
        return module;
    }

    public ButtonGroupCardModule buttons() {
        CardBuilder.checkMaxSize(this.modules, 10);
        ButtonGroupCardModule module = new ButtonGroupCardModule(this);
        this.modules.add(module);
        return module;
    }

    public CardItem divider() {
        return this.divider(null);
    }

    public CardItem divider(@Nullable String text) {
        CardBuilder.checkMaxSize(this.modules, 10);
        DividerCardModule module = new DividerCardModule(this, text);
        this.modules.add(module);
        return this;
    }

    public CardItem countdown(CountdownMode mode, long time) {
        CardBuilder.checkMaxSize(this.modules, 10);
        CountdownCardModule module = new CountdownCardModule(this, mode, time);
        this.modules.add(module);
        return this;
    }

    public CardBuilder end() {
        CardBuilder.checkEmpty(this.modules);
        return this.parent;
    }
}
