package net.cjsah.bot

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.cjsah.bot.event.Event
import net.cjsah.bot.event.events.AppHeartBeatEvent
import net.cjsah.bot.parser.ReceivedMsgParserBuilder
import net.cjsah.bot.util.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("Main")
val client = HttpClient(CIO) { install(WebSockets) }
var session: DefaultClientWebSocketSession? = null

suspend fun connect() {
    session = client.webSocketSession(method = HttpMethod.Get, host = "127.0.0.1", port = 1111, path = "/")
}

suspend fun main() {
    connect()

    Event.subscribe(AppHeartBeatEvent::class.java) {
        println("heart")
        println(it.heartBeat)
    }

    CoroutineScope(Dispatchers.IO).launch{
        while(true) {
            try {
                val receivedMsg = (session?.incoming?.receive() as? Frame.Text)?.readText() ?: ""
                if (receivedMsg.isEmpty()) continue
                val json = JsonUtil.deserialize(receivedMsg)
                println(json)
                ReceivedMsgParserBuilder.parse(json)
            } catch (e: Exception) {
                log.error("Error!", e)
            }
        }
    }


    println("send")


    while (!Signal.isStop());



//    thread(name = "receive") {
//        runBlocking {
//            while(true) {
//                val othersMessage = session?.incoming?.receive() as? Frame.Text
//                println(othersMessage?.readText())
////                val myMessage = Scanner(System.`in`).next()
////                if(myMessage != null) {
////                    send(myMessage)
////                }
//            }
//
//        }
//    }
}