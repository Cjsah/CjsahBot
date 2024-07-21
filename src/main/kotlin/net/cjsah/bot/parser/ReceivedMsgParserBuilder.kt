package net.cjsah.bot.parser

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import net.cjsah.bot.data.notice.GroupFileUpload
import net.cjsah.bot.data.meta.HeartBeat
import net.cjsah.bot.event.Event
import net.cjsah.bot.event.events.AppHeartBeatEvent
import net.cjsah.bot.util.JsonUtil

class ReceivedMsgParserBuilder(
    private val node: ReceivedMsgParser
) {

    companion object {
        private val root = init()

        @JvmStatic
        fun parse(raw: ObjectNode) {
            root.parse(raw)
        }

        private fun init(): ReceivedMsgParser {
            return node("post_type") {
                parser("message", isLast = true)
                parser("notice", "notice_type") {
                    parser("notify", "sub_type") {
                        parser("poke", isLast = true, run = {
                            val bean = JsonUtil.convert(it, GroupFileUpload::class.java)
                        })
                        parser("lucky_king", isLast = true, run = {
                            val bean = JsonUtil.convert(it, GroupFileUpload::class.java)
                        })
                        parser("honor", isLast = true, run = {
                            val bean = JsonUtil.convert(it, GroupFileUpload::class.java)
                        })
                    }
                    parser("group_upload", isLast = true, run = { //TODO
                        val bean = JsonUtil.convert(it, GroupFileUpload::class.java)
                    })
                    parser("group_admin", isLast = true, run = {
                        val bean = JsonUtil.convert(it, GroupFileUpload::class.java)
                    })
                    parser("group_decrease", isLast = true, run = {
                        val bean = JsonUtil.convert(it, GroupFileUpload::class.java)
                    })
                    parser("group_increase", isLast = true, run = {
                        val bean = JsonUtil.convert(it, GroupFileUpload::class.java)
                    })
                    parser("group_ban", isLast = true, run = {
                        val bean = JsonUtil.convert(it, GroupFileUpload::class.java)
                    })
                    parser("friend_add", isLast = true, run = {
                        val bean = JsonUtil.convert(it, GroupFileUpload::class.java)
                    })
                    parser("group_recall", isLast = true, run = {
                        val bean = JsonUtil.convert(it, GroupFileUpload::class.java)
                    })
                    parser("group_recall", isLast = true, run = {
                        val bean = JsonUtil.convert(it, GroupFileUpload::class.java)
                    })
                }
                parser("request", isLast = true)
                parser("meta_event", "meta_event_type") {
                    parser("heartbeat", isLast = true, run = {
                        val bean = JsonUtil.convert(it, HeartBeat::class.java)
                        Event.broadcast(AppHeartBeatEvent(bean.interval, bean.status))
                    })
                }
            }

        }

        private fun node(
            nextKey: String,
            isLast: Boolean = false,
            nextProcess: (node: JsonNode) -> Any = { it.asText() },
            run: (json: ObjectNode) -> Unit = {},
            block: ReceivedMsgParserBuilder.() -> Unit = {}
        ): ReceivedMsgParser {
            val node = ReceivedMsgParser(isLast, nextKey, nextProcess, run)
            val builder = ReceivedMsgParserBuilder(node)
            block(builder)
            return node
        }
    }

    fun <T> parser(
        value: T,
        nextKey: String = "",
        isLast: Boolean = false,
        nextProcess: (node: JsonNode) -> Any = { it.asText() },
        run: (json: ObjectNode) -> Unit = {},
        block: ReceivedMsgParserBuilder.() -> Unit = {}
    ) {
        val node = ReceivedMsgParser(isLast, nextKey, nextProcess, run)
        val builder = ReceivedMsgParserBuilder(node)
        this.node.addParser(value, node)
        block(builder)
    }
}
