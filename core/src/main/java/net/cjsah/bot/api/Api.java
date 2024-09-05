package net.cjsah.bot.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.util.JsonUtil;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class Api {
    private static String TOKEN = "";

    public static void sendMsg() {
        post("https://chat.xiaoheihe.cn/chatroom/v2/channel_msg/send", json -> {
            json.put("msg", "");
            json.put("msg_type", "");
            json.put("heychat_ack_id", "");
            json.put("reply_id", "");
            json.put("room_id", "");
            json.put("addition", "");
            json.put("at_user_id", "");
            json.put("at_role_id", "");
            json.put("mention_channel_id", "");
            json.put("channel_id", "");
            json.put("channel_type", "");
        });
    }

    public static String getToken() {
        return TOKEN;
    }

    public static void setToken(String TOKEN) {
        Api.TOKEN = TOKEN;
    }

    private static JSONObject post(String url, Consumer<JSONObject> consumer) {
        HttpRequest request = HttpRequest.post(url)
                .header("Content-Type", "application/json;charset=UTF-8;")
                .timeout(5000);
        request.header("token", TOKEN);
        JSONObject body = new JSONObject();
        consumer.accept(body);
        request.body(JsonUtil.serialize(body));
        try (HttpResponse response = request.execute()) {
            String bodyStr = new String(response.bodyBytes(), StandardCharsets.UTF_8);
            return JsonUtil.deserialize(bodyStr);
        }
    }
}
