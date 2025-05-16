package net.cjsah.bot.http;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class BotHttpServerImpl {
    protected static final Logger log = LoggerFactory.getLogger("BotHttpServer");
    private final HttpServer httpServer;
    private static String botSecret;

    public BotHttpServerImpl() throws IOException {
        this.httpServer = HttpServer.create();
        this.httpServerBindContext();
    }

    public void init(int port, String botSecret) throws IOException {
        this.httpServer.bind(new InetSocketAddress(port), 0);
        BotHttpServerImpl.botSecret = botSecret;
    }

    public void start() {
        this.httpServer.start();
    }

    public void shutdown() {
        this.httpServer.stop(30);
    }

    private void httpServerBindContext() {
        this.httpServer.createContext("/", exchange -> {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            if (!"POST".equals(method) || !"/".equals(path)) {
                this.response(exchange, 404);
                return;
            }
            InputStream is = exchange.getRequestBody();
            String body = IoUtil.read(is, StandardCharsets.UTF_8);
            if (Validator.isEmpty(body)) {
                this.response(exchange, 401);
                return;
            }
            JSONObject json = JSON.parseObject(body);
            ExchangeResolver resolver = new ExchangeResolver(
                    exchange,
                    body,
                    exchange.getRequestHeaders(),
                    json.getIntValue("op", -1),
                    json,
                    botSecret
            );

            JSONObject res = Opcode.processRequest(resolver);
            this.response(exchange, res);
        });
    }

    private void response(HttpExchange exchange, int code) throws IOException {
        exchange.sendResponseHeaders(code, -1);
        exchange.close();
    }

    private void response(HttpExchange exchange, JSONObject json) throws IOException {
        if (json == null) {
            this.response(exchange, 200);
            return;
        }
        byte[] bytes = json.toJSONBBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream responseBody = exchange.getResponseBody()) {
            responseBody.write(bytes);
        }
        exchange.close();
    }
}
