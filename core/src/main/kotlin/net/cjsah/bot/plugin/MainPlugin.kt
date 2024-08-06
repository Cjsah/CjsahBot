package net.cjsah.bot.plugin

import net.cjsah.bot.Signal
import net.cjsah.bot.api.Api
import net.cjsah.bot.command.CommandManager
import net.cjsah.bot.command.source.ConsoleCommandSource
import net.cjsah.bot.command.source.GroupCommandSource
import net.cjsah.bot.command.source.UserCommandSource
import net.cjsah.bot.data.GroupSourceData
import net.cjsah.bot.event.EventManager
import net.cjsah.bot.event.events.AppHeartBeatEvent
import net.cjsah.bot.event.events.GroupUserJoinEvent
import net.cjsah.bot.event.events.GroupUserLeaveEvent
import net.cjsah.bot.event.events.MessageEvent
import net.cjsah.bot.heart
import net.cjsah.bot.log
import java.util.*

class MainPlugin : Plugin() {

    override fun onLoad() {
        EventManager.subscribe(INSTANCE, AppHeartBeatEvent::class.java) {
            Signal.fromStatus(it.status)
            heart?.heart(it.interval)
        }

        EventManager.subscribe(INSTANCE, MessageEvent.GroupMessageEvent::class.java) {
            log.info("[群] [${it.groupId}] [${it.userId}(${it.sender.card})] => ${it.message}")
            if (it.rawMessage.startsWith("/")) {
                val group = Api.getGroupInfo(it.groupId, false)
                val user = Api.getGroupMemberInfo(it.groupId, it.userId, false)
                val source = GroupCommandSource(GroupSourceData(group, user))
                CommandManager.execute(it.rawMessage.substring(1), source)
            }
        }

        EventManager.subscribe(INSTANCE, MessageEvent.FriendMessageEvent::class.java) {
            log.info("[好友] [${it.userId}(${it.sender.nickname})] => ${it.message}")
            if (it.rawMessage.startsWith("/")) {
                val source = UserCommandSource(it.sender)
                CommandManager.execute(it.rawMessage.substring(1), source)
            }
        }

        EventManager.subscribe(INSTANCE, GroupUserJoinEvent::class.java) {
            log.info("[群] ${it.userId} 加入群 [${it.groupId}]")
        }

        EventManager.subscribe(INSTANCE, GroupUserLeaveEvent::class.java) {
            log.info("[群] ${it.userId} 离开了群 [${it.groupId}]")
        }

        CommandManager.register { dispatcher ->
            dispatcher.register(CommandManager.literal("console").then(CommandManager.literal("stop").executes("关闭Bot") {
                Signal.stop()
            }))
            dispatcher.register(CommandManager.literal("help").executes("帮助") { context ->
                val helps = dispatcher.getHelp(context.source)
                if (context.source is ConsoleCommandSource) {
                    helps.entries.forEach { context.source.sendFeedback("${it.key}\t${it.value}") }
                } else {
                    val collect = helps.entries.joinToString("\n") { "${it.key}\t${it.value}" }
                    context.source.sendFeedback(collect)
                }
            })

//            dispatcher.register(CommandManager.literal("test").then(CommandManager.literal("xml").executes("测试xml消息") { context ->
//                val text = FileUtil.readUtf8String(File("test.xml"))
//                val msg = MessageChain.of(XMLMessageNode(text))
//                context.source.sendFeedback(msg)
//            }).then(CommandManager.literal("json").executes("测试json消息") { context ->
//                val text = FileUtil.readUtf8String(File("test.json"))
//                val msg = MessageChain.of(JsonMessageNode(text))
//                println(msg.toJson())
//                context.source.sendFeedback(msg)
//            }))

        }
//
//        EventManager.subscribe((INSTANCE), MessageEvent.GroupMessageEvent::class.java) {
//            if (it.groupId == 799652476L && it.rawMessage == "/api") {
//                Api.sendGroupMsg(it.groupId, MessageChain.raw("此消息为机器人发送"))
//            }
//
//        }
    }

    companion object {
        @JvmField
        val INSTANCE = MainPlugin()
        @JvmField
        val PLUGIN_INFO = PluginInfo(
            "main",
            "Main",
            "Main",
            "1.0",
            Collections.singletonMap<String, Any>("authors", listOf("Cjsah"))
        )
    }

}