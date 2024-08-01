package net.cjsah.bot.api;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.MainKt;
import net.cjsah.bot.data.AppVersionInfo;
import net.cjsah.bot.data.CredentialsData;
import net.cjsah.bot.data.FriendData;
import net.cjsah.bot.data.GroupData;
import net.cjsah.bot.data.GroupUserData;
import net.cjsah.bot.data.GroupUserHonorData;
import net.cjsah.bot.data.Message;
import net.cjsah.bot.data.UserBaseData;
import net.cjsah.bot.data.UserData;
import net.cjsah.bot.msg.MessageChain;
import net.cjsah.bot.util.JsonUtil;

import java.util.List;

@SuppressWarnings("unused")
public final class Api {

    /**
     * 发送私聊消息
     */
    public static int sendPrivateMsg(long qq, MessageChain message) {
        Object res = MainKt.request(ApiParam.create("send_private_msg")
                .param("user_id", qq)
                .param("message", message.toJson()));
        return ((JSONObject)res).getIntValue("message_id");
    }

    /**
     * 发送群消息
     */
    public static int sendGroupMsg(long group, MessageChain message) {
        Object res = MainKt.request(ApiParam.create("send_group_msg")
                .param("group_id", group)
                .param("message", message.toJson()));
        return ((JSONObject) res).getIntValue("message_id");
    }

    /**
     * 撤回消息
     */
    public static void RecallMsg(int messageId) {
        MainKt.request(ApiParam.create("delete_msg")
                .param("message_id", messageId), false);
    }

    /**
     * 获取消息
     */
    public static Message getMsg(int messageId) {
        Object res = MainKt.request(ApiParam.create("get_msg")
                .param("message_id", messageId));
        return JsonUtil.convert(res, Message.class);
    }

    /**
     * 获取合并转发消息
     */
    public static MessageChain getForwardMsg(String id) {
        Object res = MainKt.request(ApiParam.create("get_forward_msg")
                .param("id", id));
        return JsonUtil.convert(res, MessageChain.class);
    }

    /**
     * 发送好友赞
     */
    public static void sendLike(long qq, int count) {
        MainKt.request(ApiParam.create("send_like")
                .param("user_id", qq)
                .param("times", count), false);

    }

    /**
     * 群组踢人
     */
    public static void groupKickUser(long group, long qq) {
        groupKickUser(group, qq, false);
    }

    /**
     * 群组踢人
     * @param ban: 拒绝此人的加群请求
     */
    public static void groupKickUser(long group, long qq, boolean ban) {
        MainKt.request(ApiParam.create("set_group_kick")
                .param("group_id", group)
                .param("user_id", qq)
                .param("reject_add_request", ban), false);
    }

    /**
     * 群成员禁言
     */
    public static void groupMuteUser(long group, long qq, int duration) {
        MainKt.request(ApiParam.create("set_group_ban")
                .param("group_id", group)
                .param("user_id", qq)
                .param("duration", duration), false);
    }

    /**
     * 群成员解除禁言
     */
    public static void groupCancelMuteUser(long group, long qq) {
        groupMuteUser(group, qq, 0);
    }

    /**
     * 群组匿名用户禁言
     */
    public static void groupMuteAnonymous(long group, String flag, int duration) {
        MainKt.request(ApiParam.create("set_group_anonymous_ban")
                .param("group_id", group)
                .param("flag", flag)
                .param("duration", duration), false);
    }

    /**
     * 群组全员禁言
     */
    public static void groupMute(long group, boolean mute) {
        MainKt.request(ApiParam.create("set_group_whole_ban")
                .param("group_id", group)
                .param("enable", mute), false);
    }

    /**
     * 群组设置管理员
     */
    public static void setGroupAdmin(long group, long qq, boolean set) {
        MainKt.request(ApiParam.create("set_group_admin")
                .param("group_id", group)
                .param("user_id", qq)
                .param("enable", set), false);
    }

    /**
     * 设置群组是否允许匿名聊天
     */
    public static void setGroupAnonymous(long group, boolean enable) {
        MainKt.request(ApiParam.create("set_group_anonymous")
                .param("group_id", group)
                .param("enable", enable), false);
    }

    /**
     * 设置群名片（群备注）
     */
    public static void setGroupCard(long group, long qq, String card) {
        MainKt.request(ApiParam.create("set_group_card")
                .param("group_id", group)
                .param("user_id", qq)
                .param("card", card));
    }

    /**
     * 设置群名
     */
    public static void setGroupName(long group, long qq, String name) {
        MainKt.request(ApiParam.create("set_group_name")
                .param("group_id", group)
                .param("group_name", name), false);
    }

    /**
     * 退出群组
     */
    public static void setGroupLeave(long group) {
        setGroupLeave(group, false);
    }

    /**
     * 退出群组
     * @param dismiss: 是否解散该群 (群主可用)
     */
    public static void setGroupLeave(long group, boolean dismiss) {
        MainKt.request(ApiParam.create("set_group_leave")
                .param("group_id", group)
                .param("is_dismiss", dismiss), false);
    }

    /**
     * 设置群组专属头衔
     */
    public static void setGroupSpecialTitle(long group, long qq, String title) {
        MainKt.request(ApiParam.create("set_group_special_title")
                .param("group_id", group)
                .param("user_id", qq)
                .param("special_title", title), false);
    }

    /**
     * 处理加好友请求
     */
    public static void setFriendAddRequest(String flag, boolean approve, String remark) {
        MainKt.request(ApiParam.create("set_friend_add_request")
                .param("flag", flag)
                .param("approve", approve)
                .param("remark", remark), false);
    }

    /**
     * 处理加群请求／邀请
     */
    public static void setGroupAddRequest(String flag, String type, boolean approve, String reason) {
        MainKt.request(ApiParam.create("set_group_add_request")
                .param("flag", flag)
                .param("type", type)
                .param("approve", approve)
                .param("reason", reason), false);
    }

    /**
     * 获取登录号信息
     */
    public static UserBaseData getLoginInfo() {
        Object res = MainKt.request(ApiParam.create("get_login_info"));
        return JsonUtil.convert(res, UserBaseData.class);
    }

    /**
     * 获取陌生人信息
     */
    public static UserData getStrangerInfo(long qq, boolean noCache) {
        Object res = MainKt.request(ApiParam.create("get_stranger_info")
                .param("user_id", qq)
                .param("no_cache", noCache));
        return JsonUtil.convert(res, UserData.class);
    }

    /**
     * 获取好友列表
     */
    public static List<FriendData> getFriendList() {
        Object res = MainKt.request(ApiParam.create("get_friend_list"));
        return JsonUtil.convertList(res, FriendData.class);
    }

    /**
     * 获取群信息
     */
    public static GroupData getGroupInfo(long group, boolean noCache) {
        Object res = MainKt.request(ApiParam.create("get_group_info")
                .param("group_id", group)
                .param("no_cache", noCache));
        return JsonUtil.convert(res, GroupData.class);
    }

    /**
     * 获取群列表
     */
    public static List<GroupData> getGroupList() {
        Object res = MainKt.request(ApiParam.create("get_group_list"));
        return JsonUtil.convertList(res, GroupData.class);
    }

    /**
     * 获取群成员信息
     */
    public static GroupUserData getGroupMemberInfo(long group, long qq, boolean noCache) {
        Object res = MainKt.request(ApiParam.create("get_group_member_info")
                .param("group_id", group)
                .param("user_id", qq)
                .param("no_cache", noCache));
        return JsonUtil.convert(res, GroupUserData.class);
    }

    /**
     * 获取群成员列表
     */
    public static List<GroupUserData> getGroupMemberList(long group) {
        Object res = MainKt.request(ApiParam.create("get_group_member_list")
                .param("group_id", group));
        return JsonUtil.convertList(res, GroupUserData.class);
    }

    /**
     * 获取群荣誉信息
     */
    public static GroupUserHonorData getGroupHonorInfo(long group) {
        Object res = MainKt.request(ApiParam.create("get_group_honor_info")
                .param("group_id", group)
                .param("type", "all"));
        return GroupUserHonorData.fromJson((JSONObject) res);
    }

    /**
     * 获取 QQ 相关接口凭证
     */
    public static CredentialsData getCredentials(String domain) {
        Object res = MainKt.request(ApiParam.create("get_credentials")
                .param("domain", domain));
        return JsonUtil.convert(res, CredentialsData.class);
    }

    /**
     * 检查是否可以发送图片
     */
    public static boolean canSendImage() {
        Object res = MainKt.request(ApiParam.create("can_send_image"));
        return ((JSONObject) res).getBooleanValue("yes");
    }

    /**
     * 检查是否可以发送语音
     */
    public static boolean canSendRecord() {
        Object res = MainKt.request(ApiParam.create("can_send_record"));
        return ((JSONObject) res).getBooleanValue("yes");
    }

    /**
     * 获取版本信息
     */
    public static AppVersionInfo getVersionInfo() {
        Object res = MainKt.request(ApiParam.create("get_version_info"));
        return JsonUtil.convert(res, AppVersionInfo.class);
    }

    /**
     * 重启 OneBot 实现
     */
    public static void restart() {
        MainKt.request(ApiParam.create("set_restart")
                .param("delay", 500), false);
    }

    /**
     * 清理缓存
     */
    public static void cleanCache() {
        MainKt.request(ApiParam.create("clean_cache"), false);
    }
}
