package net.cjsah.bot.ext;

import net.cjsah.bot.command.Command;
import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.plugin.Plugin;
import net.cjsah.bot.util.DateUtil;

import java.util.Random;

public class TestPlugin extends Plugin {

    @Override
    public void onLoad() {
        CommandManager.register(TestPlugin.class);
    }

    @Command("/jrrp")
    public static void jrrp(CommandSource<?> source) {
        int sender = source.getSender().getId();
        String date = DateUtil.format(DateUtil.now(),"yyyy-MM-dd");
        Random random = new Random(date.hashCode() + sender);
        int rp = random.nextInt(500) % 101;
        source.sendFeedback("@{id:" + sender + "} 您今日的人品值为: " + rp);
    }

    @Override
    public void onStarted() {
        System.out.println("started");
    }

    @Override
    public void onUnload() {
        System.out.println("unload");
    }
}
