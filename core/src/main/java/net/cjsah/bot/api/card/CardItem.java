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
import java.util.function.Consumer;

public class CardItem {
    private final List<AbstractCardModule> modules;

    public CardItem() {
        this.modules = new ArrayList<>(10);
    }

    public JSONObject generate() {
        CardBuilder.checkEmpty(this.modules);
        List<JSONObject> modules = this.modules.stream().parallel().map(AbstractCardModule::generate).toList();
        return JSONObject.of("type", "card", "modules", modules);
    }

    public CardItem section(Consumer<SectionCardModule> factory) {
        CardBuilder.checkMaxSize(this.modules, 10);
        SectionCardModule module = new SectionCardModule();
        factory.accept(module);
        module.endCheck();
        this.modules.add(module);
        return this;
    }

    public CardItem header(TextType type, String text) {
        CardBuilder.checkMaxSize(this.modules, 10);
        HeaderCardModule module = new HeaderCardModule(type, text);
        this.modules.add(module);
        return this;
    }

    public CardItem images(String... urls) {
        CardBuilder.checkMaxSize(this.modules, 10);
        ImagesCardModule module = new ImagesCardModule(urls);
        this.modules.add(module);
        return this;
    }

    public CardItem images(List<String> urls) {
        CardBuilder.checkMaxSize(this.modules, 10);
        ImagesCardModule module = new ImagesCardModule(urls);
        this.modules.add(module);
        return this;
    }

    public CardItem images(List<String> urls, Consumer<ImagesCardModule> factory) {
        CardBuilder.checkMaxSize(this.modules, 10);
        ImagesCardModule module = new ImagesCardModule(urls);
        factory.accept(module);
        module.endCheck();
        this.modules.add(module);
        return this;
    }

    public CardItem images(Consumer<ImagesCardModule> factory) {
        CardBuilder.checkMaxSize(this.modules, 10);
        ImagesCardModule module = new ImagesCardModule();
        factory.accept(module);
        module.endCheck();
        this.modules.add(module);
        return this;
    }

    public CardItem buttons(Consumer<ButtonGroupCardModule> factory) {
        CardBuilder.checkMaxSize(this.modules, 10);
        ButtonGroupCardModule module = new ButtonGroupCardModule();
        factory.accept(module);
        module.endCheck();
        this.modules.add(module);
        return this;
    }

    public CardItem divider() {
        return this.divider(null);
    }

    public CardItem divider(@Nullable String text) {
        CardBuilder.checkMaxSize(this.modules, 10);
        DividerCardModule module = new DividerCardModule(text);
        this.modules.add(module);
        return this;
    }

    public CardItem countdown(CountdownMode mode, long time) {
        CardBuilder.checkMaxSize(this.modules, 10);
        CountdownCardModule module = new CountdownCardModule(mode, time);
        this.modules.add(module);
        return this;
    }

    public void endCheck() {
        CardBuilder.checkEmpty(this.modules);
    }
}
