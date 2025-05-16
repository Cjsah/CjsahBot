package net.cjsah.bot.ext;

import net.cjsah.bot.FilePaths;
import net.cjsah.bot.command.Command;
import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.ext.crawler.MCServerStatus;
import net.cjsah.bot.ext.crawler.MCVersion;
import net.cjsah.bot.ext.crawler.MojiraSearcher;
import net.cjsah.bot.plugin.Plugin;
import net.cjsah.bot.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class CrawlerPlugin extends Plugin {
    public static final Logger log = LoggerFactory.getLogger(CrawlerPlugin.class);
    private static final FilePaths.AppFile ConfigFile = FilePaths.regFile(FilePaths.CONFIG.resolve("MC-Crawler.json"), "{}");

    @Override
    public void onLoad() {
        CommandManager.register(CrawlerPlugin.class);
    }

    @Command("/jrrp")
    public static void jrrp(CommandSource<?> source) {
        long senderId = source.getSenderId();
        String date = DateUtil.format(DateUtil.now(), "yyyy-MM-dd");
        Random random = new Random(date.hashCode() + senderId);
        int rp = random.nextInt(101);
        source.sendFeedback("您今日的人品值为: " + rp);
    }

    @Command("/mcv")
    public static void mcv(CommandSource<?> source) {
        MCVersion.getLatestVersion(source, ConfigFile);
    }

    @Command("/mcs <ip>")
    public static void mcs(String ip, CommandSource<?> source) {
        if (!ip.matches("([^/:]+)(:\\d*)?")) {
            source.sendFeedback("地址格式错误!");
            return;
        }
        MCServerStatus.getServerStatus(ip, source);
    }

    @Command("/mcbug <code>")
    public static void mcbug(int code, CommandSource<?> source) {
        MojiraSearcher.getLatestVersion(code, source);
    }

}
