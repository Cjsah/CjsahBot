package net.cjsah.bot.parser

import com.alibaba.fastjson2.JSONObject
import net.cjsah.bot.event.EventManager
import net.cjsah.bot.event.events.AppConnectedEvent
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

class ReceivedEventParser(
    private val node: ReceivedEventParserNode
) {

    companion object {
        private val root = init()

        @JvmStatic
        fun parse(raw: JSONObject) {
            root.parse(raw)
        }

        private fun init(): ReceivedEventParserNode {
            return node("post_type") {
                parser("meta_event", "meta_event_type") {
                    parser("lifecycle", "sub_type") {
                        parser("connect", isLast = true, run = {
                            EventManager.broadcast(AppConnectedEvent(it))
                        })
                    }
                    parser("heartbeat", isLast = true, run = {
                        EventManager.broadcast(AppHeartBeatEvent(it))
                    })
                }
                parser("notice", "notice_type") {
                    parser("group_upload", isLast = true, run = {
                        EventManager.broadcast(GroupFileUploadEvent(it))
                    })
                    parser("group_admin", "sub_type") {
                        parser("set", isLast = true, run = {
                            EventManager.broadcast(GroupAdminSetEvent(it))
                        })
                        parser("unset", isLast = true, run = {
                            EventManager.broadcast(GroupAdminUnsetEvent(it))
                        })
                    }
                    parser("group_decrease", "sub_type") {
                        parser("leave", isLast = true, run = {
                            EventManager.broadcast(GroupUserSelfLeaveEvent(it))
                        })
                        parser("kick", isLast = true, run = {
                            EventManager.broadcast(GroupUserKickEvent(it))
                        })
                        parser("kick_me", isLast = true, run = {
                            EventManager.broadcast(GroupUserKickMeEvent(it))
                        })
                    }
                    parser("group_increase", "sub_type") {
                        parser("approve", isLast = true, run = {
                            EventManager.broadcast(GroupUserApproveJoinEvent(it))
                        })
                        parser("invite", isLast = true, run = {
                            EventManager.broadcast(GroupUserInviteJoinEvent(it))
                        })
                    }
                    parser("group_ban", "sub_type") {
                        parser("ban", isLast = true, run = {
                            EventManager.broadcast(GroupMuteAppendEvent(it))
                        })
                        parser("lift_ban", isLast = true, run = {
                            EventManager.broadcast(GroupMuteRemoveEvent(it))
                        })
                    }
                    parser("friend_add", isLast = true, run = {
                        EventManager.broadcast(FriendAppendedEvent(it))
                    })
                    parser("group_recall", isLast = true, run = {
                        EventManager.broadcast(GroupMsgRecallEvent(it))
                    })
                    parser("friend_recall", isLast = true, run = {
                        EventManager.broadcast(FriendMsgRecallEvent(it))
                    })
                    parser("notify", "sub_type") {
                        parser("poke", isLast = true, run = {
                            EventManager.broadcast(GroupPokeEvent(it))
                        })
                        parser("lucky_king", isLast = true, run = {
                            EventManager.broadcast(GroupRedpackLuckyEvent(it))
                        })
                        parser("honor", "honor_type") {
                            parser("talkative", isLast = true, run = {
                                EventManager.broadcast(GroupUserDragonHonorEvent(it))
                            })
                            parser("performer", isLast = true, run = {
                                EventManager.broadcast(GroupUserChatFireHonorEvent(it))
                            })
                            parser("emotion", isLast = true, run = {
                                EventManager.broadcast(GroupUserHappinessHonorEvent(it))
                            })
                        }
                    }
                }
                parser("message", "message_type") {
                    parser("private", "sub_type") {
                        parser("friend", isLast = true, run = {
                            EventManager.broadcast(FriendNormalMessageEvent(it))
                        })
                        parser("group", isLast = true, run = {
                            EventManager.broadcast(FriendTemporaryMessageEvent(it))
                        })
                        parser("other", isLast = true, run = {
                            EventManager.broadcast(FriendOtherMessageEvent(it))
                        })
                    }
                    parser("group", "sub_type") {
                        parser("normal", isLast = true, run = {
                            EventManager.broadcast(GroupNormalMessageEvent(it))
                        })
                        parser("anonymous", isLast = true, run = {
                            EventManager.broadcast(GroupAnonymousMessageEvent(it))
                        })
                        parser("notice", isLast = true, run = {
                            EventManager.broadcast(GroupNoticeMessageEvent(it))
                        })
                    }
                }
                parser("request", "request_type") {
                    parser("friend", isLast = true, run = {
                        EventManager.broadcast(FriendAppendRequestEvent(it))
                    })
                    parser("group", "sub_type") {
                        parser("add", isLast = true, run = {
                            EventManager.broadcast(GroupAppendNormalRequestEvent(it))
                        })
                        parser("invite", isLast = true, run = {
                            EventManager.broadcast(GroupAppendInviteRequestEvent(it))
                        })
                    }
                }
            }

        }

        private fun node(
            nextKey: String,
            isLast: Boolean = false,
            run: (json: JSONObject) -> Unit = {},
            block: ReceivedEventParser.() -> Unit = {}
        ): ReceivedEventParserNode {
            val node = ReceivedEventParserNode(isLast, nextKey, run)
            val builder = ReceivedEventParser(node)
            block(builder)
            return node
        }
    }

    fun parser(
        value: String,
        nextKey: String = "",
        isLast: Boolean = false,
        run: (json: JSONObject) -> Unit = {},
        block: ReceivedEventParser.() -> Unit = {}
    ) {
        val node = ReceivedEventParserNode(isLast, nextKey, run)
        val builder = ReceivedEventParser(node)
        this.node.addParser(value, node)
        block(builder)
    }
}
