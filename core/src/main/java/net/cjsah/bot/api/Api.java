package net.cjsah.bot.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.MemeData;
import net.cjsah.bot.data.RoomRole;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.util.JsonUtil;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "UnusedReturnValue", "DuplicatedCode"})
public final class Api {
    private static String TOKEN = "";

    public static String sendMsg(MsgBuilder builder) {
        JSONObject res = post("https://chat.xiaoheihe.cn/chatroom/v2/channel_msg/send", json -> {
            json.put("room_id", builder.getRoomId());
            json.put("channel_id", builder.getChannelId());
            json.put("msg", builder.getMsg());
            json.put("reply_id", builder.getReplay());
            json.put("at_user_id", builder.getAt());
            json.put("msg_type", 10);
            json.put("heychat_ack_id", builder.getUuid());
            json.put("addition", "{}");
            json.put("at_role_id", "");
            json.put("mention_channel_id", "");
            json.put("channel_type", 1);
        });
        return res.getJSONObject("result").getString("msg_id");
    }

    public static List<RoomRole> getRoomRoles(String roomId) {
        JSONObject res = get("https://chat.xiaoheihe.cn/chatroom/v2/room_role/roles", map -> map.put("room_id", roomId));
        return res.getJSONObject("result").getList("roles", JSONObject.class).stream().map(RoomRole::new).toList();
    }

    public static RoomRole createRoomRole(RoomRoleBuilder builder) {
        JSONObject res = post("https://chat.xiaoheihe.cn/chatroom/v2/room_role/create", json -> {
            json.put("room_id", builder.getRoomId());
            json.put("name", builder.getName());
            json.put("icon", builder.getIconUrl());
            json.put("permissions", builder.getPermissions());
            json.put("type", 0);
            json.put("hoist", builder.isHoist());
            json.put("nonce", builder.getUuid());
            List<Color> colors = builder.getColors();
            if (colors.size() == 1) {
                json.put("color", colors.getFirst().getRGB());
            } else if (colors.size() > 1) {
                json.put("color_list", colors);
            }
        });
        return new RoomRole(res.getJSONObject("result").getJSONObject("role"));
    }

    public static RoomRole updateRoomRole(RoomRoleBuilder builder) {
        JSONObject res = post("https://chat.xiaoheihe.cn/chatroom/v2/room_role/update", json -> {
            json.put("id", builder.getId());
            json.put("room_id", builder.getRoomId());
            json.put("name", builder.getName());
            json.put("icon", builder.getIconUrl());
            json.put("permissions", builder.getPermissions());
            json.put("type", 0);
            json.put("position", builder.getPosition());
            json.put("hoist", builder.isHoist());
            json.put("nonce", builder.getUuid());
            List<Color> colors = builder.getColors();
            if (colors.size() == 1) {
                json.put("color", colors.getFirst().getRGB());
            } else if (colors.size() > 1) {
                json.put("color_list", colors);
            }
        });
        return new RoomRole(res.getJSONObject("result").getJSONObject("role"));
    }

    public static void deleteRoomRole(String roleId, String roomId) {
        post("https://chat.xiaoheihe.cn/chatroom/v2/room_role/delete", json -> {
            json.put("role_id", roleId);
            json.put("room_id", roomId);
        });
    }

    public static void userGiveRole(int userId, String roomId, String roleId) {
        post("https://chat.xiaoheihe.cn/chatroom/v2/room_role/grant", json -> {
            json.put("to_user_id", userId);
            json.put("role_id", roleId);
            json.put("room_id", roomId);
        });
    }

    public static void userRevokeRole(int userId, String roomId, String roleId) {
        post("https://chat.xiaoheihe.cn/chatroom/v2/room_role/revoke", json -> {
            json.put("to_user_id", userId);
            json.put("role_id", roleId);
            json.put("room_id", roomId);
        });
    }

    public static List<MemeData> getMemeList(String roomId) {
        JSONObject res = get("https://chat.xiaoheihe.cn/chatroom/v3/msg/meme/room/list", map -> map.put("room_id", roomId));
        JSONObject data = res.getJSONObject("result");
        List<MemeData> results = new ArrayList<>();
        appendToList(results, data, "emoji");
        appendToList(results, data, "sticker");
        return results;
    }

    private static void appendToList(List<MemeData> results, JSONObject json, String key) {
        List<JSONObject> array = json.getList(key, JSONObject.class);
        if (array == null || array.isEmpty()) return;
        results.addAll(array.stream().map(MemeData::new).toList());
    }

    @Deprecated
    public static void deleteMeme(String roomId, String path) {
        post("https://chat.xiaoheihe.cn/chatroom/v2/msg/meme/room/del", json -> {
            json.put("room_id", roomId);
            json.put("path", path);
        });
    }

    @Deprecated
    public static void updateMeme(String roomId, String path, String name) {
        post("https://chat.xiaoheihe.cn/chatroom/v2/msg/meme/room/edit", json -> {
            json.put("room_id", roomId);
            json.put("path", path);
            json.put("name", name);
        });
    }

    public static String getToken() {
        return TOKEN;
    }

    public static void setToken(String TOKEN) {
        Api.TOKEN = TOKEN;
    }

    private static JSONObject get(String url, Consumer<Map<String, String>> form) {
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
        Map<String, String> headers = new HashMap<>();
        form.accept(headers);
        headers.forEach(request::form);
        return Api.request(request);
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
        return Api.request(request);
    }

    private static JSONObject request(HttpRequest request) {
        try (HttpResponse response = request.execute()) {
            String bodyStr = new String(response.bodyBytes(), StandardCharsets.UTF_8);
            JSONObject json = JsonUtil.deserialize(bodyStr);
            if (!"ok".equals(json.getString("status"))) {
                throw BuiltExceptions.REQUEST_FAILED.create(json.getString("msg"));
            }
            return json;
        }
    }
}
