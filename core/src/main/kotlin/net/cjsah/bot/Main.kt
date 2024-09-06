package net.cjsah.bot

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import net.cjsah.bot.api.Api
import net.cjsah.bot.api.ApiParam
import net.cjsah.bot.permission.PermissionManager
import net.cjsah.bot.plugin.PluginLoader
import net.cjsah.bot.plugin.PluginThreadPools
import net.cjsah.bot.util.CoroutineScopeUtil
import net.cjsah.bot.util.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.collections.HashMap
import kotlin.collections.set

internal val log: Logger = LoggerFactory.getLogger("Main")
private val client = HttpClient(CIO) {
    install(WebSockets)
}
private var session: DefaultClientWebSocketSession? = null
private val msgScope = CoroutineScopeUtil.newThread()
private var job: Job? = null
internal var heart: HeartBeatTimer? = null

internal val callbacks = HashMap<String, Channel<Any?>>()

internal suspend fun main() {
    FilePaths.init()
    PermissionManager.init()
    PluginLoader.loadPlugins()

    tryConnect()

    PluginLoader.onStarted()

//    Api.getVersionInfo()
//    val id = Api.sendPrivateMsg(2684117397L, MessageChain.raw("测试"))
//    log.info("id={}", id)
//    Api.sendGroupMsg(799652476L, MessageChain.raw("测试"))

    Api.sendMsg("@{id:3595194642557722626} @{id:66956739} 1111\n\n# 0\n" +
            "## 1\n" +
            "### aa\n" +
            "- bbb\n" +
            "  - cc\n" +
            "    dd\n" +
            "1. ccc\n" +
            "2. d\n" +
            "\n" +
            "> dsdsd\n" +
            "`test`\n" +
            "'''sss'''\n" +
            "''sss''", "3595194642503450624", "3595194642524176386")

    Signal.waitStop()

    PluginLoader.unloadPlugins()
    PluginThreadPools.shutdown()
    heart?.cancel()
    session?.close()
    session = null
    job?.join()
    msgScope.shutdown()
    log.info("Closed")
}

internal suspend fun tryConnect() {
    heart?.cancel()
    session?.close()
    session = null
    log.info("正在连接到服务器...")
    while (Signal.isRunning()) {
        try {
            val content = FilePaths.ACCOUNT.read()
            val json = JsonUtil.deserialize(content)
            val token = json.getString("token");
            Api.setToken(token)
            session = client.webSocketSession(
                "wss://chat.xiaoheihe.cn/chatroom/ws/connect?chat_os_type=bot"
            )
            {
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

//            session = client.webSocketSession(
//                "wss://chat.xiaoheihe.cn/chatroom/ws/connect"
//            ) {
//                headers {
//                    append("chat_os_type", "bot")
//                    append("client_type", "heybox_chat")
//                    append("chat_version", "999.0.0")
//                    append("chat_version", "1.24.5")
//                    append("token", token)
//                }
//            }

            log.info("连接成功!")
            break
        } catch (e: Exception) {
            log.warn("连接失败, 将在 3 秒后重试...", e)
            delay(3_000)
        }
    }

    heart = HeartBeatTimer({ session?.outgoing?.trySend(Frame.Text("PING")) }) {
        runBlocking {
            tryConnect()
        }
    }.also { it.start() }

    job = msgScope.scope.launch {
        while (session != null) {
            try {
                val receivedMsg = (session?.incoming?.receive() as? Frame.Text)?.readText() ?: ""
                if (receivedMsg.isEmpty()) continue
                if (receivedMsg == "PONG") {
                    heart?.heartPong()
                    continue
                }

                val json = JsonUtil.deserialize(receivedMsg)
                if (json.getIntValue("type") == 50) {
                    println(json.toString(JSONWriter.Feature.PrettyFormat))
                }
            //                if (json.containsKey("retcode")) {
//                    ReceivedCallbackParser.parse(json)
//                } else {
//                    ReceivedEventParser.parse(json)
//                }
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

@JvmOverloads
internal fun request(form: ApiParam, callback: Boolean = true): Any {
    try {
        val session = session?.outgoing
        var result: Any? = null
        if (session != null) {
            val body = form.generate()
            val uuid = form.echo
            var channel: Channel<Any?>? = null
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
        return result ?: JSONObject()
    }catch (e: Exception) {
        log.error("Send Error!", e)
        return JSONObject()
    }
}
