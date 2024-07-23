package net.cjsah.bot

import com.alibaba.fastjson2.JSONObject
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.cjsah.bot.api.Api
import net.cjsah.bot.api.ApiParam
import net.cjsah.bot.event.Event
import net.cjsah.bot.event.events.AppHeartBeatEvent
import net.cjsah.bot.msg.MessageChain
import net.cjsah.bot.parser.ReceivedCallbackParser
import net.cjsah.bot.parser.ReceivedEventParser
import net.cjsah.bot.util.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

internal val log: Logger = LoggerFactory.getLogger("Main")
internal val client = HttpClient(CIO) { install(WebSockets) }
internal var session: DefaultClientWebSocketSession? = null
internal var heart: HeartBeatTimer? = null

internal suspend fun main() {
    tryConnect()

    Event.subscribe(AppHeartBeatEvent::class.java) {
        Signal.fromStatus(it.status)
        heart?.heart(it.interval)
    }

//    Api.sendPrivateMsg(2684117397L, MessageChain.raw("测试"))
    Api.sendGroupMsg(799652476L, MessageChain.raw("测试"))


    while (Signal.isRunning());

}

internal suspend fun tryConnect() {
    heart?.stop()
    session?.close()
    session = null
    while (true) {
        try {
            session = client.webSocketSession(method = HttpMethod.Get, host = "127.0.0.1", port = 1111, path = "/")
            break
        } catch (e: Exception) {
            log.warn("连接失败, 将在 3 秒后重试...", e)
            delay(3_000)
        }
    }

    heart = HeartBeatTimer(5000) {
        CoroutineScope(Dispatchers.Main).launch {
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
            }
        }
        if (Signal.isRunning()) {
            log.warn("Session is closed!")
        }
    }
}

@JvmOverloads
internal fun request(form: ApiParam, callback: Boolean = true): JSONObject {
    try {
        val session = session?.outgoing
        if (session != null) {
            val body = form.generate()
            val result = session.trySend(Frame.Text(body))
            result.getOrThrow()
        }
    }catch (e: Exception) {
        log.error("Send Error!", e)
    }
    return JSONObject()
}
