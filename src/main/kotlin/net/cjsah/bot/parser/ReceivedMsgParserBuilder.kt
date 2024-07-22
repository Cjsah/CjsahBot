package net.cjsah.bot.parser

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import net.cjsah.bot.event.Event
import net.cjsah.bot.event.events.AppHeartBeatEvent
import net.cjsah.bot.event.events.AppendRequestEvent.FriendAppendRequestEvent
import net.cjsah.bot.event.events.AppendRequestEvent.GroupAppendRequestEvent.GroupAppendInviteRequestEvent
import net.cjsah.bot.event.events.AppendRequestEvent.GroupAppendRequestEvent.GroupAppendNormalRequestEvent
import net.cjsah.bot.event.events.FriendAppendedEvent
import net.cjsah.bot.event.events.GroupAdminChangeEvent.GroupAdminSetEvent
import net.cjsah.bot.event.events.GroupAdminChangeEvent.GroupAdminUnsetEvent
import net.cjsah.bot.event.events.GroupFileUploadEvent
import net.cjsah.bot.event.events.GroupMuteEvent.GroupMuteAppendEvent
import net.cjsah.bot.event.events.GroupMuteEvent.GroupMuteRemoveEvent
import net.cjsah.bot.event.events.GroupPokeEvent
import net.cjsah.bot.event.events.GroupRedpackLuckyEvent
import net.cjsah.bot.event.events.GroupUserHonorEvent.GroupUserChatFireHonorEvent
import net.cjsah.bot.event.events.GroupUserHonorEvent.GroupUserDragonHonorEvent
import net.cjsah.bot.event.events.GroupUserHonorEvent.GroupUserHappinessHonorEvent
import net.cjsah.bot.event.events.GroupUserJoinEvent.GroupUserApproveJoinEvent
import net.cjsah.bot.event.events.GroupUserJoinEvent.GroupUserInviteJoinEvent
import net.cjsah.bot.event.events.GroupUserLeaveEvent.GroupUserKickEvent
import net.cjsah.bot.event.events.GroupUserLeaveEvent.GroupUserKickMeEvent
import net.cjsah.bot.event.events.GroupUserLeaveEvent.GroupUserSelfLeaveEvent
import net.cjsah.bot.event.events.MessageEvent.FriendMessageEvent.FriendNormalMessageEvent
import net.cjsah.bot.event.events.MessageEvent.FriendMessageEvent.FriendOtherMessageEvent
import net.cjsah.bot.event.events.MessageEvent.FriendMessageEvent.FriendTemporaryMessageEvent
import net.cjsah.bot.event.events.MessageEvent.GroupMessageEvent.GroupAnonymousMessageEvent
import net.cjsah.bot.event.events.MessageEvent.GroupMessageEvent.GroupNormalMessageEvent
import net.cjsah.bot.event.events.MessageEvent.GroupMessageEvent.GroupNoticeMessageEvent
import net.cjsah.bot.event.events.MsgRecallEvent.FriendMsgRecallEvent
import net.cjsah.bot.event.events.MsgRecallEvent.GroupMsgRecallEvent

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
                        Event.broadcast(FriendAppendedEvent(it))
                    })
                    parser("group_recall", isLast = true, run = {
                        Event.broadcast(GroupMsgRecallEvent(it))
                    })
                    parser("friend_recall", isLast = true, run = {
                        Event.broadcast(FriendMsgRecallEvent(it))
                    })
                    parser("notify", "sub_type") {
                        parser("poke", isLast = true, run = {
                            Event.broadcast(GroupPokeEvent(it))
                        })
                        parser("lucky_king", isLast = true, run = {
                            Event.broadcast(GroupRedpackLuckyEvent(it))
                        })
                        parser("honor", "honor_type") {
                            parser("talkative", isLast = true, run = {
                                Event.broadcast(GroupUserDragonHonorEvent(it))
                            })
                            parser("performer", isLast = true, run = {
                                Event.broadcast(GroupUserChatFireHonorEvent(it))
                            })
                            parser("emotion", isLast = true, run = {
                                Event.broadcast(GroupUserHappinessHonorEvent(it))
                            })
                        }
                    }
                }
                parser("message", "message_type") {
                    parser("private", "sub_type") {
                        parser("friend", isLast = true, run = {
                            Event.broadcast(FriendNormalMessageEvent(it))
                        })
                        parser("group", isLast = true, run = {
                            Event.broadcast(FriendTemporaryMessageEvent(it))
                        })
                        parser("other", isLast = true, run = {
                            Event.broadcast(FriendOtherMessageEvent(it))
                        })
                    }
                    parser("group", "sub_type") {
                        parser("normal", isLast = true, run = {
                            Event.broadcast(GroupNormalMessageEvent(it))
                        })
                        parser("anonymous", isLast = true, run = {
                            Event.broadcast(GroupAnonymousMessageEvent(it))
                        })
                        parser("notice", isLast = true, run = {
                            Event.broadcast(GroupNoticeMessageEvent(it))
                        })
                    }
                }
                parser("request", "request_type") {
                    parser("friend", isLast = true, run = {
                        Event.broadcast(FriendAppendRequestEvent(it))
                    })
                    parser("group", "sub_type") {
                        parser("add", isLast = true, run = {
                            Event.broadcast(GroupAppendNormalRequestEvent(it))
                        })
                        parser("invite", isLast = true, run = {
                            Event.broadcast(GroupAppendInviteRequestEvent(it))
                        })
                    }
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
