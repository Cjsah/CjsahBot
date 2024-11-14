package net.cjsah.bot.api.card.modules;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.CardBuilder;
import net.cjsah.bot.api.card.AbstractCardModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImagesCardModule extends AbstractCardModule {
    private final List<String> urls;

    public ImagesCardModule() {
        super("images");
        this.urls = new ArrayList<>(18);
    }

    public ImagesCardModule(String... urls) {
        this();
        CardBuilder.checkArraySize(urls, 18);
        Collections.addAll(this.urls, urls);
    }

    public ImagesCardModule(List<String> urls) {
        this();
        CardBuilder.checkMaxSize(urls, 18);
        this.urls.addAll(urls);
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
    public void endCheck() {
        CardBuilder.checkEmpty(this.urls);
    }
}
