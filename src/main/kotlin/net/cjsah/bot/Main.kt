package net.cjsah.bot

import com.alibaba.fastjson2.JSONObject
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.cjsah.bot.api.ApiParam
import net.cjsah.bot.event.Event
import net.cjsah.bot.event.events.AppHeartBeatEvent
import net.cjsah.bot.event.events.MessageEvent
import net.cjsah.bot.parser.ReceivedCallbackParser
import net.cjsah.bot.parser.ReceivedEventParser
import net.cjsah.bot.util.CoroutineScopeUtil
import net.cjsah.bot.util.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

internal val log: Logger = LoggerFactory.getLogger("Main")
internal val client = HttpClient(CIO) { install(WebSockets) }
internal var session: DefaultClientWebSocketSession? = null
internal var heart: HeartBeatTimer? = null

internal val callbacks = HashMap<String, Channel<JSONObject>>()

internal suspend fun main() {
    tryConnect()

    Event.subscribe(AppHeartBeatEvent::class.java) {
        Signal.fromStatus(it.status)
        heart?.heart(it.interval)
    }

    Event.subscribe(MessageEvent.GroupMessageEvent::class.java) {
        log.info("[群] [${it.groupId}] [${it.userId}(${it.sender.card})] => ${it.message}")
    }

    Event.subscribe(MessageEvent.FriendMessageEvent::class.java) {
        log.info("[好友] [${it.userId}(${it.sender.nickname})] => ${it.message}")
    }

//    Api.sendPrivateMsg(2684117397L, MessageChain.raw("测试"))
//    Api.sendGroupMsg(799652476L, MessageChain.raw("测试"))


    while (Signal.isRunning());

}

internal suspend fun tryConnect() {
    heart?.stop()
    session?.close()
    session = null
    log.info("正在连接到服务器...")
    while (true) {
        try {
            session = client.webSocketSession(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/?access_token=")
            log.info("连接成功!")
            break
        } catch (e: Exception) {
            log.warn("连接失败, 将在 3 秒后重试...", e)
            delay(3_000)
        }
    }

    heart = HeartBeatTimer(5000) {
        CoroutineScopeUtil.launch {
            tryConnect()
        }
    }

    CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher()).launch {
        while(session != null) {
            try {
                val receivedMsg = (session?.incoming?.receive() as? Frame.Text)?.readText() ?: ""
                if (receivedMsg.isEmpty()) continue
                val json = JsonUtil.deserialize(receivedMsg)
                if (json.containsKey("retcode")) {
                    ReceivedCallbackParser.parse(json)
                } else {
                    ReceivedEventParser.parse(json)
                }
            } catch (e: Exception) {
                log.error("Error!", e)
                if (e is ClosedReceiveChannelException) {
                    break
                }
            }
        }
        if (Signal.isRunning()) {
            log.warn("Session is closed!")
        }
    }
}

@JvmOverloads
internal fun request(form: ApiParam, callback: Boolean = true): JSONObject? {
    try {
        val session = session?.outgoing
        var result: JSONObject? = null
        if (session != null) {
            val body = form.generate()
            val uuid = form.echo
            var channel: Channel<JSONObject>? = null
            if (callback) {
                channel = Channel()
                callbacks[uuid] = channel
            }
            val sendResult = session.trySend(Frame.Text(body))
            sendResult.getOrThrow()
            runBlocking {
                result = channel?.receive()
            }
        }
        return result
    }catch (e: Exception) {
        log.error("Send Error!", e)
        return JSONObject()
    }
}
