package net.cjsah.bot.ext;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.command.Command;
import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.plugin.Plugin;
import net.cjsah.bot.util.DateUtil;
import net.cjsah.bot.util.JsonUtil;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TestPlugin extends Plugin {

    @Override
    public void onLoad() {
        CommandManager.register(TestPlugin.class);

//        EventManager.subscribe(UserModifyEvent.class, event -> {
//            switch (event.getState()) {
//                case IN -> {
//                    Api.sendMsg(new MsgBuilder(event.getRoomInfo().getId(), "???", "欢迎@{id:" + event.getUserInfo().getId() + "}加入房间")
//                            .at(event.getUserInfo().getId()));
//                }
//                case OUT -> {
//                    Api.sendMsg(new MsgBuilder(event.getRoomInfo().getId(), "???", "@{id:" + event.getUserInfo().getId() + "}离开了房间")
//                            .at(event.getUserInfo().getId()));
//                }
//            }
//        });

    }

    @Command("/jrrp")
    public static void jrrp(CommandSource source) {
        long sender = source.sender().getSenderInfo().getId();
        String date = DateUtil.format(DateUtil.now(),"yyyy-MM-dd");
        Random random = new Random(date.hashCode() + sender);
        int rp = random.nextInt(101);
        source.sendFeedback("@{id:" + sender + "} 您今日的人品值为: " + rp);
    }

    @Command("/mcv")
    public static void mcv(CommandSource source) {
        HttpRequest request = HttpRequest.get("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        try (HttpResponse response = request.execute()) {
            if (response.getStatus() != 200) {
                source.sendFeedback("获取最新mc版本失败: " + response.getStatus());
                return;
            }
            String body = response.body();
            JSONObject latest = JsonUtil.deserialize(body).getJSONObject("latest");
            String release = latest.getString("release");
            String snapshot = latest.getString("snapshot");
            source.sendFeedback("当前最新正式版本: %s\n\n当前最新快照版本: %s".formatted(release, snapshot));
        }
    }

    @Command("/mcs <ip>")
    public static void mcs(String ip, CommandSource source) {
        if (!ip.matches("([^/:]+)(:\\d*)?")) {
            source.sendFeedback("地址格式错误!");
            return;
        }
        String[] list = ip.split(":");
        switch (list.length) {
            case 1 -> getInfo(ip, 25565, source);
            case 2 -> getInfo(list[0], Integer.parseInt(list[1]), source);
            default -> source.sendFeedback("地址格式错误!");
        }
    }

    private static void getInfo(String ip, int port, CommandSource source) {
        JSONObject info = ServerUtil.requestInfo(ip, port);
        String error = info.getString("error");
        if (error != null) {
            source.sendFeedback("获取失败: " + error);
            return;
        }
        JSONObject players = info.getJSONObject("players");
        StringBuilder builder = new StringBuilder();
        Object description = info.get("description");
        if (description instanceof String value) {
            builder.append(value);
        } else if (description instanceof JSONObject json) {
            builder.append(json.getString("text"));
            if (json.containsKey("extra")) {
                List<JSONObject> extra = json.getList("extra", JSONObject.class);
                builder.append(extra.stream()
                        .map(it -> it.getString("text"))
                        .collect(Collectors.joining("\n", "\n", "\n")));
            }
        }
        String text = builder.toString().replaceAll("\\u00a7([a-zA-Z0-9])", "");
        String val = Constants.MC_SERVER_MSG.formatted(
                ip, port,
                info.getJSONObject("version").getString("name"),
                players.getIntValue("online"),
                players.getIntValue("max"),
                text
        );

        source.sendFeedback(val);
    }

    @Command("/mcbug <code>")
    public static void mcv(int code, CommandSource source) {
        HttpRequest request = HttpRequest.get("https://bugs.mojang.com/rest/api/2/issue/MC-" + code);
        try (HttpResponse response = request.execute()) {
            if (response.getStatus() != 200) {
                source.sendFeedback("获取mc bug信息失败: " + response.getStatus());
                return;
            }
            String body = response.body();
            JSONObject fields = JsonUtil.deserialize(body).getJSONObject("fields");
            String versions = fields.getList("versions", JSONObject.class)
                    .stream()
                    .map(it -> it.getString("name")
                            .replace("Minecraft ", "")
                            .replace("Pre-Release ", "Pre-")
                            .replace("Release Candidate ", "RC-"))
                    .collect(Collectors.joining(" "));
            String val = Constants.MC_BUG_MSG.formatted(
                    code,
                    fields.getString("summary"),
                    getName(fields, "issuetype", "未知"),
                    getName(fields, "status", "未知"),
                    getName(fields, "resolution", "未解决"),
                    getName(fields, "reporter", "未知"),
                    versions,
                    fields.getString("description")
            );
            source.sendFeedback(val);
        }
    }

    private static String getName(JSONObject json, String key, String defaultValue) {
        JSONObject node = json.getJSONObject(key);
        if (node == null) return defaultValue;
        return node.getString("name");
    }
}
