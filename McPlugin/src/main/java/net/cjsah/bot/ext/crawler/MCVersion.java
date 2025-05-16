package net.cjsah.bot.ext.crawler;

import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import net.cjsah.bot.FilePaths;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.ext.CrawlerPlugin;
import net.cjsah.bot.util.RequestUtil;
import org.jetbrains.annotations.Nullable;

public class MCVersion {

    public static void getLatestVersion(CommandSource<?> source, FilePaths.AppFile ConfigFile) {
        Version version = checkUpdate(ConfigFile);
        source.sendFeedback("\n当前最新正式版本: %s\n当前最新快照版本: %s".formatted(version.release, version.snapshot));
    }

    private static Version checkUpdate(FilePaths.AppFile ConfigFile) {
        JSONObject res = RequestUtil.getRequest("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        JSONObject latest = res.getJSONObject("latest");
        String release = latest.getString("release");
        String snapshot = latest.getString("snapshot");

        JSONObject config = JSON.parseObject(ConfigFile.read());
        JSONObject savedLatest = config.getJSONObject("latest");
        String savedRelease = savedLatest.getString("release");
        String savedSnapshot = savedLatest.getString("snapshot");

        String newVersion = null;

        if (!Validator.equal(savedRelease, release)) {
            newVersion = release;
        } else if (!Validator.equal(savedSnapshot, snapshot)) {
            newVersion = snapshot;
        }

        if (newVersion != null) {
            savedLatest.put("release", release);
            savedLatest.put("snapshot", snapshot);
            ConfigFile.write(config.toJSONString(JSONWriter.Feature.PrettyFormat));
            CrawlerPlugin.log.info("发现新版本:{}", newVersion);
        }

        return new Version(release, snapshot, newVersion);
    }

    record Version(String release, String snapshot, @Nullable String newVersion) {
    }
}
