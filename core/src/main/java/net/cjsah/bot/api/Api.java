package net.cjsah.bot.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.data.MemeData;
import net.cjsah.bot.data.RoleInfo;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "UnusedReturnValue", "DuplicatedCode"})
public final class Api {
    private static final Logger log = LoggerFactory.getLogger(Api.class);
    private static String TOKEN = "";

    public static String sendMsg(MsgBuilder builder) {
        return Api.sendMsg(builder, true);
    }

    public static String sendMsg(MsgBuilder builder, boolean log) {
        if (log) Api.log.info("[{}] [{}] <== {}", builder.getRoomId(), builder.getChannelId(), builder.getMsg());
        JSONObject res = postJson("https://chat.xiaoheihe.cn/chatroom/v2/channel_msg/send", json -> {
            json.put("channel_type", 1);
            json.put("msg_type", 10);
            json.put("room_id", builder.getRoomId());
            json.put("channel_id", builder.getChannelId());
            json.put("msg", builder.getMsg());
            json.put("reply_id", builder.getReplay());
            json.put("at_user_id", builder.getAtUsers());
            json.put("at_role_id", builder.getAtRoles());
            json.put("mention_channel_id", builder.getAtChannels());
            json.put("heychat_ack_id", builder.getUuid());
            json.put("addition", "{}");
        });
        return res.getJSONObject("result").getString("msg_id");
    }

    public static String uploadMedia(File file) {
        log.info("上传文件: {}", file.getAbsolutePath());
        JSONObject res = postForm("https://chat-upload.xiaoheihe.cn/upload", request -> request.form("file", file));
        return res.getJSONObject("result").getString("url");
    }

    public static String uploadMedia(String filename, byte[] data) {
        log.info("上传文件: {}", filename);
        JSONObject res = postForm("https://chat-upload.xiaoheihe.cn/upload", request -> request.form("file", data, filename));
        return res.getJSONObject("result").getString("url");
    }

    public static List<RoleInfo> getRoomRoles(String roomId) {
        JSONObject res = get("https://chat.xiaoheihe.cn/chatroom/v2/room_role/roles", map -> map.put("room_id", roomId));
        return res.getJSONObject("result").getList("roles", JSONObject.class).stream().map(RoleInfo::new).toList();
    }

    public static RoleInfo createRoomRole(RoomRoleBuilder builder) {
        JSONObject res = postJson("https://chat.xiaoheihe.cn/chatroom/v2/room_role/create", json -> {
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
        return new RoleInfo(res.getJSONObject("result").getJSONObject("role"));
    }

    public static RoleInfo updateRoomRole(RoomRoleBuilder builder) {
        JSONObject res = postJson("https://chat.xiaoheihe.cn/chatroom/v2/room_role/update", json -> {
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
        return new RoleInfo(res.getJSONObject("result").getJSONObject("role"));
    }

    public static void deleteRoomRole(String roleId, String roomId) {
        postJson("https://chat.xiaoheihe.cn/chatroom/v2/room_role/delete", json -> {
            json.put("role_id", roleId);
            json.put("room_id", roomId);
        });
    }

    public static void userGiveRole(int userId, String roomId, String roleId) {
        postJson("https://chat.xiaoheihe.cn/chatroom/v2/room_role/grant", json -> {
            json.put("to_user_id", userId);
            json.put("role_id", roleId);
            json.put("room_id", roomId);
        });
    }

    public static void userRevokeRole(int userId, String roomId, String roleId) {
        postJson("https://chat.xiaoheihe.cn/chatroom/v2/room_role/revoke", json -> {
            json.put("to_user_id", userId);
            json.put("role_id", roleId);
            json.put("room_id", roomId);
        });
    }

    public static List<MemeData> getMemeList(String roomId) {
        JSONObject res = get("https://chat.xiaoheihe.cn/chatroom/v3/msg/meme/room/list", form -> form.put("room_id", roomId));
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

    public static void deleteMeme(String roomId, String path) {
        postJson("https://chat.xiaoheihe.cn/chatroom/v2/msg/meme/room/del", json -> {
            json.put("room_id", roomId);
            json.put("path", path);
        });
    }

    public static void updateMeme(String roomId, String path, String name) {
        postJson("https://chat.xiaoheihe.cn/chatroom/v2/msg/meme/room/edit", json -> {
            json.put("room_id", roomId);
            json.put("path", path);
            json.put("name", name);
        });
    }

    public static void msgReplyEmoji(String roomId, String channelId, String msgId, String emoji, boolean append) {
        postJson("https://chat.xiaoheihe.cn/chatroom/v2/channel_msg/emoji/reply", json -> {
            json.put("room_id", roomId);
            json.put("channel_id", channelId);
            json.put("msg_id", msgId);
            json.put("emoji", emoji);
            json.put("is_add", append ? 1 : 2);
        });
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
        HttpRequest request = Api.genRequest(url, Method.POST);
        JSONObject body = new JSONObject();
        consumer.accept(body);
        request.body(JsonUtil.serialize(body));
        return Api.request(request);
    }

    private static JSONObject postForm(String url, Consumer<HttpRequest> consumer) {
        HttpRequest request = Api.genRequest(url, Method.POST);
        request.header("Content-Type", "multipart/form-data", true);
        consumer.accept(request);
        return Api.request(request);
    }

    private static HttpRequest genRequest(String url, Method method) {
        return HttpRequest.of(url + "?chat_os_type=bot")
                .method(method)
                .header("Content-Type", "application/json;charset=UTF-8;")
                .header("token", TOKEN)
                .header("client_type", "heybox_chat")
                .header("x_client_type", "web")
                .header("os_type", "web")
                .header("x_os_type", "bot")
                .header("x_app", "heybox_chat")
                .header("chat_version", "1.24.5")
                .timeout(5000);
    }

    private static JSONObject request(HttpRequest request) {
//        request.addRequestInterceptor(System.out::println); // log
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
