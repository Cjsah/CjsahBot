package net.cjsah.bot.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;

import java.nio.charset.StandardCharsets;

public final class RequestUtil {

    public static HttpRequest get(String url) {
        return request(url, Method.GET);
    }

    public static HttpRequest post(String url) {
        return request(url, Method.POST).header("Content-Type", "application/json");
    }

    public static JSONObject getRequest(String url) {
        return request(request(url, Method.GET));
    }

    public static JSONObject postRequest(String url) {
        return request(request(url, Method.GET));
    }

    public static HttpRequest request(String url, Method method) {
        return HttpRequest.of(url).method(method).timeout(5000);
    }

    public static JSONObject request(HttpRequest request) {
        try (HttpResponse response = request.execute()) {
            String bodyStr = new String(response.bodyBytes(), StandardCharsets.UTF_8);
            return JsonUtil.deserialize(bodyStr);
        }
    }

}
