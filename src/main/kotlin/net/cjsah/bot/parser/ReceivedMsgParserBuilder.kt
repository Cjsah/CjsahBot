package net.cjsah.bot.parser

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import net.cjsah.bot.event.Event
import net.cjsah.bot.event.events.AppHeartBeatEvent
import net.cjsah.bot.event.events.GroupAdminChangeEvent.GroupAdminSetEvent
import net.cjsah.bot.event.events.GroupAdminChangeEvent.GroupAdminUnsetEvent
import net.cjsah.bot.event.events.GroupFileUploadEvent
import net.cjsah.bot.event.events.GroupMuteEvent.GroupMuteAppendEvent
import net.cjsah.bot.event.events.GroupMuteEvent.GroupMuteRemoveEvent
import net.cjsah.bot.event.events.GroupUserJoinEvent.GroupUserApproveJoinEvent
import net.cjsah.bot.event.events.GroupUserJoinEvent.GroupUserInviteJoinEvent
import net.cjsah.bot.event.events.GroupUserLeaveEvent.*

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
                        Event.broadcast(AppHeartBeatEvent(it))
                    })
                }
                parser("notice", "notice_type") {
                    parser("group_upload", isLast = true, run = {
                        Event.broadcast(GroupFileUploadEvent(it))
                    })
                    parser("group_admin", "sub_type") {
                        parser("set", isLast = true, run = {
                            Event.broadcast(GroupAdminSetEvent(it))
                        })
                        parser("unset", isLast = true, run = {
                            Event.broadcast(GroupAdminUnsetEvent(it))
                        })
                    }
                    parser("group_decrease", "sub_type") {
                        parser("leave", isLast = true, run = {
                            Event.broadcast(GroupUserSelfLeaveEvent(it))
                        })
                        parser("kick", isLast = true, run = {
                            Event.broadcast(GroupUserKickEvent(it))
                        })
                        parser("kick_me", isLast = true, run = {
                            Event.broadcast(GroupUserKickMeEvent(it))
                        })
                    }
                    parser("group_increase", "sub_type") {
                        parser("approve", isLast = true, run = {
                            Event.broadcast(GroupUserApproveJoinEvent(it))
                        })
                        parser("invite", isLast = true, run = {
                            Event.broadcast(GroupUserInviteJoinEvent(it))
                        })
                    }
                    parser("group_ban", "sub_type") {
                        parser("ban", isLast = true, run = {
                            Event.broadcast(GroupMuteAppendEvent(it))
                        })
                        parser("lift_ban", isLast = true, run = {
                            Event.broadcast(GroupMuteRemoveEvent(it))
                        })
                    }
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
