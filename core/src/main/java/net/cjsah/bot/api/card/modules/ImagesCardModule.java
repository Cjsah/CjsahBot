package net.cjsah.bot.api.card.modules;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.CardBuilder;
import net.cjsah.bot.api.card.AbstractCardModule;
import net.cjsah.bot.api.card.CardItem;
import net.cjsah.bot.data.Size;
import net.cjsah.bot.data.TextType;
import net.cjsah.bot.data.Theme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class ImagesCardModule extends AbstractCardModule {
    private final List<String> urls;

    public ImagesCardModule(CardItem parent, String... urls) {
        super(parent, "images");
        this.urls = new ArrayList<>(18);
        Collections.addAll(this.urls, urls);
    }

    public ImagesCardModule url(String url) {
        CardBuilder.checkMaxSize(this.urls, 18);
        this.urls.add(url);
        return this;
    }

    @Override
    public JSONObject generate() {
        JSONObject result = super.generate();
        List<JSONObject> urls = this.urls.stream().parallel().map(it -> JSONObject.of("url", it)).toList();
        result.put("urls", urls);
        return result;
    }

    @Override
    public CardItem end() {
        CardBuilder.checkEmpty(this.urls);
        return super.end();
    }
}
