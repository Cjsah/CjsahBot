package net.cjsah.bot.plugin;

import net.cjsah.bot.Main;
import net.cjsah.bot.SignalType;
import net.cjsah.bot.api.Api;
import net.cjsah.bot.api.CardBuilder;
import net.cjsah.bot.command.Command;
import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.command.source.CommandSource;
import net.cjsah.bot.data.CountdownMode;
import net.cjsah.bot.data.Size;
import net.cjsah.bot.data.TextType;
import net.cjsah.bot.data.Theme;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.event.events.CardButtonClickEvent;
import net.cjsah.bot.event.events.CommandEvent;
import net.cjsah.bot.permission.HeyboxPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public final class MainPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger("Console");
    public static final MainPlugin INSTANCE = new MainPlugin();
    public static final PluginInfo PLUGIN_INFO = new PluginInfo("main", "Main", "Main", "1.0", Collections.singletonMap("authors", Collections.singletonList("Cjsah")));

    @Override
    public void onLoad() {
        CommandManager.register(MainPlugin.class);

        String pluginId = PLUGIN_INFO.getId();

//        @Deprecated
//        EventManager.subscribe(pluginId, MessageEvent.class, event ->
//                log.info("[{}] [{}] [{}({})] => {}", event.getRoomName(), event.getChannelName(), event.getUserName(), event.getUserId(), event.getMsg())
//        );

//        EventManager.subscribe(pluginId, MessageEmojiPinEvent.class, event -> {
//            log.info("{} {} {} {}", event.getUserId(), event.getEmoji(), event.getChannelId(), event.getMsgId());
//        });
//
        EventManager.subscribe(pluginId, CommandEvent.class, event -> {
            log.info("[{}({})] [{}({})] [{}({})] ==> 触发命令: /{}",
                    event.getRoomInfo().getName(),
                    event.getRoomInfo().getId(),
                    event.getChannelInfo().getName(),
                    event.getChannelInfo().getId(),
                    event.getSenderInfo().getNickname(),
                    event.getSenderInfo().getId(),
                    event.getCommandInfo().getCommand()
            );
            CommandSource source = new CommandSource(event);
            CommandManager.execute(event.getCommandInfo(), source);
        });

        EventManager.subscribe(CardButtonClickEvent.class, event -> {
            log.info("room: {},channel: {}, user: {}, text: {}, value: {}",
                    event.getRoomInfo().getName(),
                    event.getChannelInfo().getName(),
                    event.getSenderInfo().getNickname(),
                    event.getButtonText(),
                    event.getButtonValue()
            );
        });
    }

    @Command(value = "/botstop", permissions = HeyboxPermission.ADMIN)
    public static void botStop(CommandSource source) {
        source.sendFeedback("bot正在关闭...");
        Main.sendSignal(SignalType.STOP);
    }


    @Command(value = "/test", permissions = HeyboxPermission.ADMIN)
    public static void test(CommandSource source) {
        CommandEvent sender = source.sender();
        String roomId = sender.getRoomInfo().getId();
        String channelId = sender.getChannelInfo().getId();
        CardBuilder builder1 = new CardBuilder(roomId, channelId)
                .replay(sender.getMsgId())
                .card(cart -> cart
                        .section(section -> section
                                .text(TextType.TEXT, "aaaa")
                        )
                        .section(section -> section
                                .text(TextType.TEXT, "bbbb")
                                .text(TextType.MARKDOWN, "`111`**222**\n\n> 3333")
                        )
                        .section(section -> section
                                .image("https://chat.max-c.com/pic/1844295655587745795.gif", Size.SMALL)
                                .text(TextType.MARKDOWN, "`111`**222**\n\n> 3333")
                        )
                        .section(section -> section
                                .text(TextType.MARKDOWN, "`111`**222**\n\n> 3333")
                                .image("https://chat.max-c.com/pic/1844295655587745795.gif", Size.LARGE)
                        )
                        .section(section -> section
                                .text(TextType.MARKDOWN, "`111`**222**\n\n> 3333")
                                .button("click", "unknown", false, Theme.DANGER)
                        )
                );
        CardBuilder builder2 = new CardBuilder(roomId, channelId)
                .replay(sender.getMsgId())
                .card(card -> card
                        .section(section -> section
                                .text(TextType.MARKDOWN, "`111`**222**\n\n> 3333")
                                .image("https://chat.max-c.com/pic/1844295655587745795.gif", Size.LARGE)
                                .endCheck()
                        )
                        .header(TextType.TEXT, "`aaaaa\n\n`一段文字` *内容*")
                        .header(TextType.MARKDOWN, "aaaaa\n\n`一段文字` *内容*")
                        .images("https://chat.max-c.com/pic/1844295655587745795.gif")
                        .divider()
                        .images("https://chat.max-c.com/pic/1844295655587745795.gif", "https://chat.max-c.com/pic/1844295655587745795.gif")
                        .divider("分割线")
                        .buttons(buttons -> buttons
                                .button("click1", "unknown", false, Theme.DANGER)
                                .button("click2", "https://server.cjsah.net:1002/", true, Theme.SUCCESS)
                                .button("click3", "https://server.cjsah.net:1002/", false, Theme.PRIMARY)
                        )
                );
        long time = System.currentTimeMillis() / 1000 + 2160000;
        CardBuilder builder3 = new CardBuilder(roomId, channelId)
                .replay(sender.getMsgId())
                .card(card -> card
                        .section(section -> section
                                .text(TextType.MARKDOWN, "`111`**222**\n\n> 3333")
                                .image("https://chat.max-c.com/pic/1844295655587745795.gif", Size.LARGE)
                        )
                        .countdown(CountdownMode.DEFAULT, time)
                )
                .card(card -> card
                        .section(section -> section
                                .text(TextType.MARKDOWN, "`111`**222**\n\n> 3333")
                                .image("https://chat.max-c.com/pic/1844295655587745795.gif", Size.LARGE)
                        )
                        .countdown(CountdownMode.CALENDAR, time)
                )
                .card(card -> card
                        .section(section -> section
                                .text(TextType.MARKDOWN, "`111`**222**\n\n> 3333")
                                .image("https://chat.max-c.com/pic/1844295655587745795.gif", Size.LARGE)
                        )
                        .countdown(CountdownMode.SECOND, time)
                );

        Api.sendCardMsg(builder1);
        Api.sendCardMsg(builder2);
        Api.sendCardMsg(builder3);


    }

}
