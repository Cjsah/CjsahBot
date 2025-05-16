package net.cjsah.bot.http;

import com.alibaba.fastjson2.JSONObject;
import com.sun.net.httpserver.Headers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum Opcode {
    DISPATCH(0, resolver -> null),
    HTTP_ACK(12, resolver -> null),
    WEBHOOK_VERIFY(13, HttpServerOperate::verifyAccount),
    ;

    Opcode(int code, Function<ExchangeResolver, JSONObject> handler) {
        this.handler = handler;
        Opcode.InnerClass.TYPE_MAP.put(code, this);
    }

    private final Function<ExchangeResolver, JSONObject> handler;

    public static JSONObject processRequest(ExchangeResolver resolver) {
        Opcode opcode = Opcode.InnerClass.TYPE_MAP.get(resolver.opcode());
        if (opcode != null) return opcode.handler.apply(resolver);
        BotHttpServerImpl.log.warn("Unknown opcode: {}, {}", resolver.opcode(), resolver.body());
        return null;
    }

    private static class InnerClass {
        private static final Map<Integer, Opcode> TYPE_MAP = new HashMap<>();
    }
}
