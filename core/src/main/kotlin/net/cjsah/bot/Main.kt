package net.cjsah.bot

import cn.hutool.core.io.FileUtil
import com.alibaba.fastjson2.JSONObject
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.cjsah.bot.api.Api
import net.cjsah.bot.api.ApiParam
import net.cjsah.bot.msg.MessageChain
import net.cjsah.bot.msg.nodes.XMLMessageNode
import net.cjsah.bot.parser.ReceivedCallbackParser
import net.cjsah.bot.parser.ReceivedEventParser
import net.cjsah.bot.plugin.PluginLoader
import net.cjsah.bot.plugin.PluginThreadPools
import net.cjsah.bot.util.CoroutineScopeUtil
import net.cjsah.bot.util.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

internal val log: Logger = LoggerFactory.getLogger("Main")
private val client = HttpClient(CIO) { install(WebSockets) }
private var session: DefaultClientWebSocketSession? = null
private val msgScope = CoroutineScopeUtil.newThread()
private var job: Job? = null
internal var heart: HeartBeatTimer? = null

internal val callbacks = HashMap<String, Channel<Any?>>()

internal suspend fun main() {
    FilePaths.init()
    PluginLoader.loadPlugins()

    tryConnect()

    PluginLoader.onStarted()

//    Api.getVersionInfo()
//    val id = Api.sendPrivateMsg(2684117397L, MessageChain.raw("测试"))
//    log.info("id={}", id)
//    Api.sendGroupMsg(799652476L, MessageChain.raw("测试"))

    Signal.waitStop()

    PluginLoader.unloadPlugins()
    PluginThreadPools.shutdown()
    heart?.stop()
    session?.close()
    session = null
    job?.join()
    msgScope.shutdown()
    log.info("Closed")
}

internal suspend fun tryConnect() {
    heart?.stop()
    session?.close()
    session = null
    log.info("正在连接到服务器...")
    while (Signal.isRunning()) {
        try {
            val content = FilePaths.ACCOUNT.read()
            val json = JsonUtil.deserialize(content)
            session = client.webSocketSession(
                method = HttpMethod.Get,
                host = json.getString("host"),
                port = json.getIntValue("port"),
                path = "/?access_token=${json.getString("token")}"
            )
            log.info("连接成功!")
            break
        } catch (e: Exception) {
            log.warn("连接失败, 将在 3 秒后重试...", e)
            delay(3_000)
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
                if (json.containsKey("retcode")) {
                    ReceivedCallbackParser.parse(json)
                } else {
                    ReceivedEventParser.parse(json)
                }
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
