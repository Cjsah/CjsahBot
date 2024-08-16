package net.cjsah.bot

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
import net.cjsah.bot.api.ApiParam
import net.cjsah.bot.parser.ReceivedCallbackParser
import net.cjsah.bot.parser.ReceivedEventParser
import net.cjsah.bot.plugin.PluginLoader
import net.cjsah.bot.plugin.PluginThreadPools
import net.cjsah.bot.util.CoroutineScopeUtil
import net.cjsah.bot.util.JsonUtil
import org.quartz.JobBuilder
import org.quartz.JobExecutionContext
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal val log: Logger = LoggerFactory.getLogger("Main")
internal val client = HttpClient(CIO) { install(WebSockets) }
internal var heart: HeartBeat? = null

internal val callbacks = HashMap<String, Channel<Any?>>()

internal val factory = StdSchedulerFactory()
internal val scheduler = factory.getScheduler()

internal suspend fun main() {
    FilePaths.init()
    PluginLoader.loadPlugins()
    scheduler.start()
    val job = JobBuilder
        .newJob(TokenJob::class.java)
        .withIdentity("TokenJob", "Main")
        .build()
    val trigger = TriggerBuilder.newTrigger()
        .withIdentity("TokenTrigger", "Main")
        .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever())
        .startNow()
        .build()
    scheduler.scheduleJob(job, trigger)

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
    client.close()
    log.info("Closed")
}

internal suspend fun tryConnect() {
    heart?.stop()
    log.info("正在连接到服务器...")
    val content = FilePaths.ACCOUNT.read()
    val json = JsonUtil.deserialize(content)
    heart = HeartBeat(json.getString("host"), json.getIntValue("port"))
}

@JvmOverloads
internal fun request(form: ApiParam, callback: Boolean = true): Any {
//    try {
//        val session = session?.outgoing
//        var result: Any? = null
//        if (session != null) {
//            val body = form.generate()
//            val uuid = form.echo
//            var channel: Channel<Any?>? = null
//            if (callback) {
//                channel = Channel()
//                callbacks[uuid] = channel
//            }
//            val sendResult = session.trySend(Frame.Text(body))
//            sendResult.getOrThrow()
//            runBlocking {
//                result = channel?.receive()
//            }
//        }
//        return result ?: JSONObject()
//    }catch (e: Exception) {
//        log.error("Send Error!", e)
//        return JSONObject()
//    }
    return JSONObject()
}

class TokenJob : org.quartz.Job {
    override fun execute(context: JobExecutionContext) {
        println(1)
    }
}