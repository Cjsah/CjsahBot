package net.cjsah.bot.parser

import com.alibaba.fastjson2.JSONObject
import kotlinx.coroutines.runBlocking
import net.cjsah.bot.callbacks
import net.cjsah.bot.log

internal class ReceivedCallbackParser(
    private val node: ReceivedEventParserNode
) {

    companion object {

        @JvmStatic
        fun parse(json: JSONObject) {
            val uuid = json.getString("echo")
            if (uuid == null || uuid.isEmpty()) {
                log.warn("Unknown callback: {}", json)
                return
            }
            val channel = callbacks[uuid]
            runBlocking {
                if (channel == null) {
                    log.warn("Channel {} not exist!", uuid)
                    return@runBlocking
                }
                channel.send(json.getJSONObject("data"))
            }
        }
    }
}
