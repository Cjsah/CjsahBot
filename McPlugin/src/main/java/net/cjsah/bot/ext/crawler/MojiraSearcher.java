package net.cjsah.bot.ext.crawler;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.PluginException;
import net.cjsah.bot.ext.CrawlerPlugin;
import net.cjsah.bot.util.RequestUtil;

import java.util.stream.Collectors;

public class MojiraSearcher {

    public static void getLatestVersion(int code, CommandSource<?> source) {
        try {
            JSONObject fields = search(code);

            String versions = fields.getList("versions", JSONObject.class)
                    .stream()
                    .map(it -> versionMap(it.getString("name")))
                    .collect(Collectors.joining(" "));

            StringBuilder builder = new StringBuilder();
            builder.append("\nMC-");
            builder.append(code);
            builder.append("\n");
            builder.append(fields.getString("summary"));
            builder.append("\n类型: ");
            builder.append(getName(fields, "issuetype", "未知"));
            builder.append("\n状态: ");
            builder.append(getName(fields, "status", "未知"));
            builder.append("\n解决结果: ");
            builder.append(getName(fields, "resolution", "未解决"));
            builder.append("\n报告人: ");
            builder.append(getName(fields, "reporter", "未知"));
            builder.append("\n影响版本: ");
            builder.append(versions);
            JSONArray fixVersions = fields.getJSONArray("fixVersions");
            if (!fixVersions.isEmpty()) {
                String version = fixVersions.getJSONObject(0).getString("name");
                builder.append("\n修复版本: ");
                builder.append(versionMap(version));
            }
            source.sendFeedback(builder.toString());
        } catch (PluginException e) {
            CrawlerPlugin.log.error(e.getMessage());
            source.sendFeedback(e.getMessage());
        }
    }

    private static JSONObject search(int code) {
        JSONObject body = JSONObject.of("project", "MC", "advanced", true, "search", "key='MC-" + code + "'");
        JSONObject res = RequestUtil.request(RequestUtil.post("https://bugs.mojang.com/api/jql-search-post")
                .header("Content-Type", "application/json")
                .body(body.toJSONString())
        );
        JSONObject node = res.getJSONArray("issues").getJSONObject(0);
        if (node == null) throw BuiltExceptions.PLUGIN_EXCEPTION.create("未找到此问题");

        return node.getJSONObject("fields");
    }

    private static String versionMap(String version) {
        return version
                .replace("Minecraft ", "")
                .replace("Pre-Release ", "Pre-")
                .replace("Release Candidate ", "RC-");
    }

    private static String getName(JSONObject json, String key, String defaultValue) {
        JSONObject node = json.getJSONObject(key);
        if (node == null) return defaultValue;
        return node.getString("name");
    }

}
