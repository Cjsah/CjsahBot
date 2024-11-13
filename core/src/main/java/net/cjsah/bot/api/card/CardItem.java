package net.cjsah.bot.api.card;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.CardBuilder;

import java.util.ArrayList;
import java.util.List;

public class CardItem {
    private final List<AbstractCardModule> modules;
    private final CardBuilder parent;

    public CardItem(CardBuilder parent) {
        this.parent = parent;
        this.modules = new ArrayList<>();
    }

    public JSONObject generate() {
        return null; // TODO
    }

    public CardBuilder end() {
        return this.parent;
    }
}
