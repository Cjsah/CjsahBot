package net.cjsah.bot

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.cjsah.bot.util.CoroutineScopeUtil
import net.cjsah.bot.util.JsonUtil

class HeartBeat(
    private val host: String,
    private val port: Int,
    private val path: String = "/",
) {
    private var session: DefaultClientWebSocketSession? = null
    private val heart: HeartBeatTimer
    private val msgScope = CoroutineScopeUtil.newThread()
    private var job: Job? = null

    init {
        runBlocking {
            while (Signal.isRunning()) {
                try {
                    session = client.webSocketSession(
                        method = HttpMethod.Get,
                        host = host,
                        port = port,
                        path = path
                    )
                    log.info("连接成功!")
                    break
                } catch (e: Exception) {
                    log.warn("连接失败, 将在 3 秒后重试...", e)
                    delay(3_000)
                }
            }
        }
        heart = HeartBeatTimer(5000) {
            tryConnect()
        }

        job = msgScope.scope.launch {
            while (session != null) {
                try {
                    val receivedMsg = (session?.incoming?.receive() as? Frame.Text)?.readText() ?: ""
                    if (receivedMsg.isEmpty()) continue
                    val json = JsonUtil.deserialize(receivedMsg)
                    println(json)
//                    if (json.containsKey("retcode")) {
//                        ReceivedCallbackParser.parse(json)
//                    } else {
//                        ReceivedEventParser.parse(json)
//                    }
                } catch (e: Exception) {
                    if (e is ClosedReceiveChannelException) {
                        break
                    }
                    log.error("Error!", e)
                }
            }
            if (Signal.isRunning()) {
                log.warn("Session is closed!")
            }
            this.cancel("")
        }
    }

    fun heart(time: Long = 0) {
        this.heart.heart(0)
    }

    suspend fun stop() {
        heart.stop()
        session?.close()
        session = null
        job?.join()
        msgScope.shutdown()
    }
}