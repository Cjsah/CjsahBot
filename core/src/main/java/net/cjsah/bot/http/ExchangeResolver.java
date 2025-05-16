package net.cjsah.bot.http;

import com.alibaba.fastjson2.JSONObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public record ExchangeResolver(HttpExchange exchange, String body, Headers headers, int opcode, JSONObject data, String secret) {
}
