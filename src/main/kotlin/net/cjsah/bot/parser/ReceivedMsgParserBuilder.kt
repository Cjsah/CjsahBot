package net.cjsah.bot.parser

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import net.cjsah.bot.event.events.AppHeartBeatEvent
import net.cjsah.bot.event.events.GroupAdminChangeEvent
import net.cjsah.bot.event.events.GroupFileUploadEvent
import net.cjsah.bot.event.events.GroupMuteEvent
import net.cjsah.bot.event.events.GroupUserJoinEvent
import net.cjsah.bot.event.events.GroupUserLeaveEvent

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
                parser("meta_event", "meta_event_type") {
                    parser("heartbeat", isLast = true, run = {
                        AppHeartBeatEvent.parse(it);
                    })
                }
                parser("notice", "notice_type") {
                    parser("group_upload", isLast = true, run = {
                        GroupFileUploadEvent.parse(it)
                    })
                    parser("group_admin", isLast = true, run = {
                        GroupAdminChangeEvent.parse(it)
                    })
                    parser("group_decrease", isLast = true, run = {
                        GroupUserLeaveEvent.parse(it)
                    })
                    parser("group_increase", isLast = true, run = {
                        GroupUserJoinEvent.parse(it)
                    })
                    parser("group_ban", isLast = true, run = {
                        GroupMuteEvent.parse(it)
                    })
                    parser("friend_add", isLast = true, run = {
                    })
                    parser("group_recall", isLast = true, run = {
                    })
                    parser("group_recall", isLast = true, run = {
                    })
                    parser("notify", "sub_type") {
                        parser("poke", isLast = true, run = {
                            })
                        parser("lucky_king", isLast = true, run = {
                            })
                        parser("honor", isLast = true, run = {
                            })
                    }
                }
                parser("message", isLast = true)
                parser("request", isLast = true)
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
