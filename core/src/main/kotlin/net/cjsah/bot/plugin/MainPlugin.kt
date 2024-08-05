package net.cjsah.bot.plugin

import net.cjsah.bot.Signal
import net.cjsah.bot.event.EventManager
import net.cjsah.bot.event.events.AppHeartBeatEvent
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
        }

        EventManager.subscribe(INSTANCE, MessageEvent.FriendMessageEvent::class.java) {
            log.info("[好友] [${it.userId}(${it.sender.nickname})] => ${it.message}")
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