package net.cjsah.bot.ext.crawler;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.util.RequestUtil;

import java.util.List;
import java.util.stream.Collectors;

public class MCServerStatus {
    private static final String UrlTemplate = "https://api.mcstatus.io/v2/status/java/";
    public static void  getServerStatus(String ip, CommandSource<?> source) {
        ServerStatus status = getServerStatus(ip);
        if (!status.online) {
            source.sendFeedback("服务器已离线!");
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append(status.motd);
        builder.append("\n游戏版本: ");
        builder.append(status.version);
        builder.append("\n在线人数: ");
        builder.append(status.onlineCount);
        builder.append("/");
        builder.append(status.maxCount);
        if (status.onlineCount > 0) {
            String players = status.players.stream().map(it -> it.name).collect(Collectors.joining("\n"));
            builder.append("\n在线列表:\n");
            builder.append(players);
        }

        source.sendFeedback(builder.toString());
    }

    private static ServerStatus getServerStatus(String ip) {
        String url = UrlTemplate + ip;
        if (!ip.contains(":")) url += ":25565";
        JSONObject res = RequestUtil.getRequest(url);
        boolean online = res.getBoolean("online");
        String motd = res.getJSONObject("motd").getString("clean");
        String version = res.getJSONObject("version").getString("name_clean");
        JSONObject playerInfo = res.getJSONObject("players");
        int onlineCount = playerInfo.getIntValue("online");
        int maxCount = playerInfo.getIntValue("max");
        List<Player> players = playerInfo.getList("list", JSONObject.class)
                .stream()
                .map(it -> new Player(it.getString("name_clean"), it.getString("uuid")))
                .toList();
        int count = players.size();
        players = players.stream()
                .filter(it -> !"Anonymous Player".equals(it.name) && !"00000000-0000-0000-0000-000000000000".equals(it.uuid))
                .collect(Collectors.toList());
        count = count - players.size();
        if (count > 0) {
            players.addFirst(new Player("Fake Player x" + count, "00000000-0000-0000-0000-000000000000"));
        }
        return new ServerStatus(online, motd, version, onlineCount, maxCount, players);
    }

    private record ServerStatus(boolean online, String motd, String version, int onlineCount, int maxCount, List<Player> players) {}

    private record Player(String name, String uuid) {}

}
