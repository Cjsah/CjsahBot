package net.cjsah.bot.ext.crawler;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.TypedMessage;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.exception.BuiltExceptions;
import net.cjsah.bot.exception.PluginException;
import net.cjsah.bot.ext.CrawlerPlugin;
import net.cjsah.bot.util.RequestUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MojiraSearcher {

    public static void getLatestVersion(int code, CommandSource<?> source) {
        try {
            CrawlerPlugin.log.info("正在获取 #MC-{}", code);
            JSONObject fields = search(code);

            String versions = fields.getList("versions", JSONObject.class)
                    .stream()
                    .map(it -> versionMap(it.getString("name")))
                    .collect(Collectors.joining(" "));

            JSONArray fixVersions = fields.getJSONArray("fixVersions");
            String fixVersion = "未修复";
            if (!fixVersions.isEmpty()) {
                String version = fixVersions.getJSONObject(0).getString("name");
                fixVersion = versionMap(version);
            }
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(code));
            params.put("title", fields.getString("summary"));
            params.put("type", getName(fields, "issuetype", "未知"));
            params.put("status", getName(fields, "status", "未知"));
            params.put("resolution", getName(fields, "resolution", "未解决"));
            params.put("reporter", getName(fields, "reporter", "未知"));
            params.put("version", versions);
            params.put("fix_version", fixVersion);

            source.sendFeedback(TypedMessage.markdown("102283152_1747463399", params));
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
