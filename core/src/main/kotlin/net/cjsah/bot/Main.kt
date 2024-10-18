package net.cjsah.bot

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import net.cjsah.bot.api.Api
import net.cjsah.bot.event.EventManager
import net.cjsah.bot.permission.PermissionManager
import net.cjsah.bot.plugin.PluginLoader
import net.cjsah.bot.plugin.PluginThreadPools
import net.cjsah.bot.util.CoroutineScopeUtil
import net.cjsah.bot.util.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal val log: Logger = LoggerFactory.getLogger("Main")
private val client = HttpClient(CIO) {
    install(WebSockets)
}
private var session: DefaultClientWebSocketSession? = null
private val msgScope = CoroutineScopeUtil.newThread()
private var job: Job? = null
internal val heart: HeartBeatTimer = HeartBeatTimer({ session?.outgoing?.trySend(Frame.Text("PING")) }) {
    runBlocking {
        tryConnect()
    }
}

internal val callbacks = HashMap<String, Channel<Any?>>()

internal suspend fun main() {
    FilePaths.init()
    PermissionManager.init()
    PluginLoader.loadPlugins()

    tryConnect()

    PluginLoader.onStarted()

    Signal.waitStop()

    PluginLoader.unloadPlugins()
    PluginThreadPools.shutdown()
    heart.cancel()
    session?.close()
    session = null
    job?.join()
    msgScope.shutdown()
    log.info("Closed")
}

internal suspend fun tryConnect() {
    heart.stop()
    session?.close()
    session = null
    log.info("正在连接到服务器...")
    while (Signal.isRunning()) {
        try {
            val content = FilePaths.ACCOUNT.read()
            val json = JsonUtil.deserialize(content)
            val token = json.getString("token")
            Api.setToken(token)
            session = client.webSocketSession("wss://chat.xiaoheihe.cn/chatroom/ws/connect?chat_os_type=bot") {
                headers {
                    append("client_type", "heybox_chat")
                    append("x_client_type", "web")
                    append("os_type", "web")
                    append("x_os_type", "bot")
                    append("x_app", "heybox_chat")
                    append("chat_version", "1.24.5")
                    append("token", token)
                }
            }
            log.info("连接成功!")
            break
        } catch (e: Exception) {
            log.warn("连接失败, 将在 3 秒后重试...", e)
            delay(3_000)
        }
    }

    heart.start()

    job = msgScope.scope.launch {
        while (session != null) {
            try {
                val receivedMsg = (session?.incoming?.receive() as? Frame.Text)?.readText() ?: ""
                if (receivedMsg.isEmpty()) continue
                if (receivedMsg == "PONG") {
                    heart.heartPong()
                    continue
                }
                val json = JsonUtil.deserialize(receivedMsg)
                EventManager.parseEvent(json)
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
