package net.cjsah.bot.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public final class Api {
    private static final Logger log = LoggerFactory.getLogger("Console");
    private static String TOKEN = "";

    public static String sendFriendMsg(String userId, String replyId, TypedMessage typedMessage) {
        JSONObject res = postJson("/v2/users/%s/messages".formatted(userId), json -> {
            json.put("msg_type", typedMessage.getType());
            json.putAll(typedMessage.getContent());
            if (replyId != null) {
                json.put("msg_id", replyId);
            }
        });
        return res.getString("id");
    }

    public static String sendGroupMsg(String groupId, String replyId, TypedMessage typedMessage) {
        JSONObject res = postJson("/v2/groups/%s/messages".formatted(groupId), json -> {
            json.put("msg_type", typedMessage.getType());
            json.putAll(typedMessage.getContent());
            if (replyId != null) {
                json.put("msg_id", replyId);
            }
        });
        System.out.println(res);
        return "";
    }

    public static void setToken(String token) {
        if (token == null || token.isEmpty()) return;
        Api.TOKEN = token;
    }

    private static JSONObject get(String url, Consumer<Map<String, String>> form) {
        HttpRequest request = Api.genRequest(url, Method.GET);
        Map<String, String> forms = new HashMap<>();
        form.accept(forms);
        forms.forEach(request::form);
        return Api.request(request);
    }

    private static JSONObject postJson(String url, Consumer<JSONObject> consumer) {
        HttpRequest request = Api.genRequest(url, Method.POST);
        JSONObject body = new JSONObject();
        consumer.accept(body);
        request.body(JsonUtil.serialize(body));
        return Api.request(request);
    }

    private static HttpRequest genRequest(String url, Method method) {
        if (TOKEN == null || TOKEN.isEmpty()) {
            throw BuiltExceptions.INVALID_TOKEN.create();
        }
        return HttpRequest.of("https://api.sgroup.qq.com" + url)
                .method(method)
                .header("Authorization", "QQBot " + TOKEN)
                .header("Content-Type", "application/json")
                .timeout(5000);
    }

    private static JSONObject request(HttpRequest request) {
        request.addRequestInterceptor(str -> log.debug("{}", str));
        try (HttpResponse response = request.execute()) {
            String bodyStr = new String(response.bodyBytes(), StandardCharsets.UTF_8);
            JSONObject json = JsonUtil.deserialize(bodyStr);
            log.debug("Response: \n\t{}", json);
            int code = json.getIntValue("code");
            if (code != 0) {
                String traceId = json.getString("trace_id");
                String message = json.getString("message");
                String errCode = json.getString("err_code");
                throw BuiltExceptions.API_REQUEST_FAILED.create(code, errCode, traceId, message);
            }
            return json;
        }
    }
}
