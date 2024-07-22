package net.cjsah.bot

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.cjsah.bot.event.Event
import net.cjsah.bot.event.events.AppHeartBeatEvent
import net.cjsah.bot.parser.ReceivedMsgParserBuilder
import net.cjsah.bot.util.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

val log: Logger = LoggerFactory.getLogger("Main")
val client = HttpClient(CIO) { install(WebSockets) }
var session: DefaultClientWebSocketSession? = null
var timer: HeartBeatTimer? = null

suspend fun main() {
    tryConnect()

    Event.subscribe(AppHeartBeatEvent::class.java) {
        Signal.fromStatus(it.status)
        timer?.heart(it.interval)
    }

    println("send")


    while (Signal.isRunning());

}

suspend fun tryConnect() {
    timer?.stop()
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

    timer = HeartBeatTimer(5000) {
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
                ReceivedMsgParserBuilder.parse(json)
            } catch (e: ClosedReceiveChannelException) {

            } catch (e: Exception) {
                log.error("Error!", e)
            }
        }
        if (Signal.isRunning()) {
            log.warn("Session is closed!")
        }
    }

}