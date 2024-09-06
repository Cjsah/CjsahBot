package net.cjsah.bot.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.util.JsonUtil;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class Api {
    private static String TOKEN = "";

    public static String sendMsg(String msg, String room, String channel) {
        JSONObject res = post("https://chat.xiaoheihe.cn/chatroom/v2/channel_msg/send", json -> {
            json.put("msg", msg);
            json.put("msg_type", 10);
            json.put("heychat_ack_id", "0"); // ?
            json.put("reply_id", "");
            json.put("room_id", room);
            json.put("addition", "{}");
            json.put("at_user_id", "66956739"); // ?
            json.put("at_role_id", "3595194642557722626"); // ?
            json.put("mention_channel_id", ""); // ?
            json.put("channel_id", channel);
            json.put("channel_type", 1);
        });
        return res.getJSONObject("result").getString("msg_id");
    }

    public static String getToken() {
        return TOKEN;
    }

    public static void setToken(String TOKEN) {
        Api.TOKEN = TOKEN;
    }

    private static JSONObject post(String url, Consumer<JSONObject> consumer) {
        HttpRequest request = HttpRequest.post(url + "?chat_os_type=bot")
                .header("Content-Type", "application/json;charset=UTF-8;")
                .header("token", TOKEN)
                .header("client_type", "heybox_chat")
                .header("x_client_type", "web")
                .header("os_type", "web")
                .header("x_os_type", "bot")
                .header("x_app", "heybox_chat")
                .header("chat_version", "1.24.5")
                .timeout(5000);
        JSONObject body = new JSONObject();
        consumer.accept(body);
        request.body(JsonUtil.serialize(body));
        try (HttpResponse response = request.execute()) {
            String bodyStr = new String(response.bodyBytes(), StandardCharsets.UTF_8);
            JSONObject json = JsonUtil.deserialize(bodyStr);
            if (!"ok".equals(json.getString("status"))) {
                throw BuiltExceptions.REQUEST_FAILED.apply(json.getString("msg"));
            }
            return json;
        }
    }
}
