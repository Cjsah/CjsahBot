package net.cjsah.bot.api;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.card.CardItem;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.util.JsonUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class CardBuilder {
    private final String uuid;
    private final String roomId;
    private final String channelId;
    private String replay;
    private final List<CardItem> cards;

    public CardBuilder(String roomId, String channelId) {
        this.uuid = IdUtil.fastSimpleUUID();
        this.roomId = roomId;
        this.channelId = channelId;
        this.cards = new ArrayList<>(3);
        this.replay = "";
    }

    public String getUuid() {
        return uuid;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getReplay() {
        return replay;
    }

    public CardBuilder replay(String replay) {
        this.replay = replay;
        return this;
    }

    public String genMsg() {
        checkEmpty(this.cards);
        List<JSONObject> list = this.cards.stream().parallel().map(CardItem::generate).toList();
        return JsonUtil.serialize(JSONObject.of("data", list));
    }

    public CardBuilder card(Consumer<CardItem> factory) {
        checkMaxSize(this.cards, 3);
        CardItem card = new CardItem();
        factory.accept(card);
        card.endCheck();
        this.cards.add(card);
        return this;
    }

    public static void checkMaxSize(Collection<?> list, int size) {
        if (list.size() >= size) {
            throw BuiltExceptions.MSG_TOO_MANY_DATA.create(size);
        }
    }

    public static void checkArraySize(Object[] list, int size) {
        if (list.length == 0) {
            throw BuiltExceptions.MSG_EMPTY_DATA.create();
        }
        if (list.length >= size) {
            throw BuiltExceptions.MSG_TOO_MANY_DATA.create(size);
        }
    }

    public static void checkEmpty(Collection<?> list) {
        if (list.isEmpty()) {
            throw BuiltExceptions.MSG_EMPTY_DATA.create();
        }
    }

}
