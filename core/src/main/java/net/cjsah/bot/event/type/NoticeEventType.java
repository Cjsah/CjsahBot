package net.cjsah.bot.event.type;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.event.events.Event;
import net.cjsah.bot.event.events.FriendAppendedEvent;
import net.cjsah.bot.event.events.FriendRecallEvent;
import net.cjsah.bot.event.events.GroupMemberJoinEvent;
import net.cjsah.bot.event.events.GroupMemberLeaveEvent;
import net.cjsah.bot.event.events.GroupMuteEvent;
import net.cjsah.bot.event.events.GroupRecallEvent;
import net.cjsah.bot.event.events.GroupUploadEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum NoticeEventType {
    GROUP_UPLOAD("group_upload", GroupUploadEvent::new),
    GROUP_ADMIN("group_admin", GroupAdminChangeEventType::toEvent),
    GROUP_DECREASE("group_decrease", GroupMemberLeaveEvent::new),
    GROUP_INCREASE("group_increase", GroupMemberJoinEvent::new),
    GROUP_BAN("group_ban", GroupMuteEvent::new),
    FRIEND_ADD("friend_add", FriendAppendedEvent::new),
    GROUP_RECALL("group_recall", GroupRecallEvent::new),
    FRIEND_RECALL("friend_recall", FriendRecallEvent::new),
    NOTIFY("notify", NotifyEventType::toEvent),
    ;

    private static final Logger log = LoggerFactory.getLogger("EventManager");

    NoticeEventType(String type, Function<JSONObject, Event> handler) {
        this.type = type;
        this.handler = handler;
        InnerClass.TYPE_MAP.put(type, this);
    }

    private final String type;
    private final Function<JSONObject, Event> handler;

    public String getType() {
        return this.type;
    }

    @Nullable
    public static Event toEvent(JSONObject raw) {
        String typeKey = raw.getString("notice_type");
        NoticeEventType type = InnerClass.TYPE_MAP.get(typeKey);
        if (type != null) return type.handler.apply(raw);
        log.warn("Unknown event type: {}, {}", typeKey, raw);
        return null;
    }

    private static class InnerClass {
        private static final Map<String, NoticeEventType> TYPE_MAP = new HashMap<>();
    }
}
