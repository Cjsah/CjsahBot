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

@SuppressWarnings({"unused", "UnusedReturnValue", "DuplicatedCode"})
public final class Api {
    private static final Logger log = LoggerFactory.getLogger("Console");
    private static String TOKEN = "";

    public static String sendFriendMsg(String userId, String msg, boolean markdown, String callbackMsgId) {
        JSONObject res = postJson("/v2/users/%s/messages".formatted(userId), json -> {
            json.put("msg_type", markdown ? 2 : 0);
            json.put(markdown ? "markdown" : "content", msg);
            if (callbackMsgId != null && !callbackMsgId.isEmpty()) {
                json.put("msg_id", callbackMsgId);
            }
        });
        return "";
    }

    public static String getToken() {
        return TOKEN;
    }

    public static void setToken(String TOKEN) {
        Api.TOKEN = TOKEN;
    }

    private static JSONObject get(String url, Consumer<Map<String, String>> form) {
        HttpRequest request = Api.genRequest(url, Method.GET);
        Map<String, String> forms = new HashMap<>();
        form.accept(forms);
        forms.forEach(request::form);
        return Api.request(request);
    }

    private static JSONObject postJson(String url, Consumer<JSONObject> consumer) {
        HttpRequest request = Api.genRequest("https://api.sgroup.qq.com" + url, Method.POST);
        JSONObject body = new JSONObject();
        consumer.accept(body);
        request.body(JsonUtil.serialize(body));
        return Api.request(request);
    }

    private static HttpRequest genRequest(String url, Method method) {
        return HttpRequest.of("https://api.sgroup.qq.com" + url)
                .method(method)
                .header("Authorization", TOKEN)
                .header("Content-Type", "application/json;charset=UTF-8;")
                .timeout(5000);
    }

    private static JSONObject request(HttpRequest request) {
//        request.addRequestInterceptor(System.out::println); // log
        try (HttpResponse response = request.execute()) {
            String bodyStr = new String(response.bodyBytes(), StandardCharsets.UTF_8);
            JSONObject json = JsonUtil.deserialize(bodyStr);
            System.out.println(json);
            if (!"ok".equals(json.getString("status"))) {
                throw BuiltExceptions.REQUEST_FAILED.create(json.getString("msg"));
            }
            return json;
        }
    }
}
