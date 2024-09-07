package net.cjsah.bot.ext;

import net.cjsah.bot.FilePaths;
import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.plugin.Plugin;
import net.cjsah.bot.util.DateUtil;

import java.nio.file.Path;
import java.util.Random;

public class TestPlugin extends Plugin {
    public static final Path CONFIG_PATH = FilePaths.CONFIG.resolve("jrrp.json");

    @Override
    public void onLoad() {
        CommandManager.register(CommandManager.literal("jrrp").executes("今日人品", context -> {
            int sender = context.getSource().getSender().getId();
            String date = DateUtil.format(DateUtil.now(),"yyyy-MM-dd");
            Random random = new Random(date.hashCode() + sender);
            int rp = random.nextInt(500) % 101;
            context.getSource().sendFeedback("@{id:" + sender + "} 您今日的人品值为: " + rp);
        }));

//        CommandManager.register(CommandManager.literal("test").executes("插件测试", context -> {
//            CommandSource<?> source = context.getSource();
//            if (source instanceof GroupCommandSource groupSource) {
//                GroupSourceData sender = groupSource.getSender();
//
//                boolean b = PermissionManager.hasPermission(PluginContext.getCurrentPluginInfo().getId(), sender.group().getGroupId(), sender.user().getUserId(), RoleType.ADMIN);
//                source.sendFeedback(String.format("你%s管理员权限", b ? "拥有" : "没有"));
//
//            }
//
//
//        }));

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
