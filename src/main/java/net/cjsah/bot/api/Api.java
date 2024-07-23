package net.cjsah.bot.api;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.MainKt;
import net.cjsah.bot.msg.MessageChain;

@SuppressWarnings("unused")
public final class Api {

    /**
     * 发送私聊消息
     */
    public static int sendPrivateMsg(long qq, MessageChain message) {
        JSONObject res = MainKt.request(ApiParam.create("send_private_msg")
                .param("user_id", qq)
                .param("message", message.toJson()));
        return res.getIntValue("message_id");
    }

    /**
     * 发送群消息
     */
    public static int sendGroupMsg(long group, MessageChain message) {
        JSONObject res = MainKt.request(ApiParam.create("send_group_msg")
                .param("group_id", group)
                .param("message", message.toJson()));
        return res.getIntValue("message_id");
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
    public static void getMsg(int messageId) {
        JSONObject res = MainKt.request(ApiParam.create("get_msg")
                .param("message_id", messageId));
    }

    /**
     * 获取合并转发消息
     */
    public static void getForwardMsg(String id) {
        JSONObject res = MainKt.request(ApiParam.create("get_forward_msg")
                .param("id", id));
    }

    /**
     * 发送好友赞
     */
    public static void sendLike(long qq, int count) {
        JSONObject res = MainKt.request(ApiParam.create("send_like")
                .param("user_id", qq)
                .param("times", count));
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
        JSONObject res = MainKt.request(ApiParam.create("set_group_kick")
                .param("group_id", group)
                .param("user_id", qq)
                .param("reject_add_request", ban));
    }

    /**
     * 群成员禁言
     */
    public static void groupMuteUser(long group, long qq, int duration) {
        JSONObject res = MainKt.request(ApiParam.create("set_group_ban")
                .param("group_id", group)
                .param("user_id", qq)
                .param("duration", duration));
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
        JSONObject res = MainKt.request(ApiParam.create("set_group_anonymous_ban")
                .param("group_id", group)
                .param("flag", flag)
                .param("duration", duration));
    }

    /**
     * 群组全员禁言
     */
    public static void groupMute(long group, boolean mute) {
        JSONObject res = MainKt.request(ApiParam.create("set_group_whole_ban")
                .param("group_id", group)
                .param("enable", mute));
    }

    /**
     * 群组设置管理员
     */
    public static void setGroupAdmin(long group, long qq, boolean set) {
        JSONObject res = MainKt.request(ApiParam.create("set_group_admin")
                .param("group_id", group)
                .param("user_id", qq)
                .param("enable", set));
    }

    /**
     * 设置群组是否允许匿名聊天
     */
    public static void setGroupAnonymous(long group, boolean enable) {
        JSONObject res = MainKt.request(ApiParam.create("set_group_anonymous")
                .param("group_id", group)
                .param("enable", enable));
    }

    /**
     * 设置群名片（群备注）
     */
    public static void setGroupCard(long group, long qq, String card) {
        JSONObject res = MainKt.request(ApiParam.create("set_group_card")
                .param("group_id", group)
                .param("user_id", qq)
                .param("card", card));
    }

    /**
     * 设置群名
     */
    public static void setGroupName(long group, long qq, String name) {
        JSONObject res = MainKt.request(ApiParam.create("set_group_name")
                .param("group_id", group)
                .param("group_name", name));
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
        JSONObject res = MainKt.request(ApiParam.create("set_group_leave")
                .param("group_id", group)
                .param("is_dismiss", dismiss));
    }

    /**
     * 设置群组专属头衔
     */
    public static void setGroupSpecialTitle(long group, long qq, String title) {
        JSONObject res = MainKt.request(ApiParam.create("set_group_special_title")
                .param("group_id", group)
                .param("user_id", qq)
                .param("special_title", title));
    }

    /**
     * 处理加好友请求
     */
    public static void setFriendAddRequest(String flag, boolean approve, String remark) {
        JSONObject res = MainKt.request(ApiParam.create("set_friend_add_request")
                .param("flag", flag)
                .param("approve", approve)
                .param("remark", remark));
    }

    /**
     * 处理加群请求／邀请
     */
    public static void setGroupAddRequest(String flag, String type, boolean approve, String reason) {
        JSONObject res = MainKt.request(ApiParam.create("set_group_add_request")
                .param("flag", flag)
                .param("type", type)
                .param("approve", approve)
                .param("reason", reason));
    }

    /**
     * 获取登录号信息
     */
    public static void getLoginInfo() {
        JSONObject res = MainKt.request(ApiParam.create("get_login_info"));
    }

    /**
     * 获取陌生人信息
     */
    public static void getStrangerInfo(long qq, boolean noCache) {
        JSONObject res = MainKt.request(ApiParam.create("get_stranger_info")
                .param("user_id", qq)
                .param("no_cache", noCache));
    }

    /**
     * 获取好友列表
     */
    public static void getFriendList() {
        JSONObject res = MainKt.request(ApiParam.create("get_friend_list"));
    }

    /**
     * 获取群信息
     */
    public static void getGroupInfo(long group, boolean noCache) {
        JSONObject res = MainKt.request(ApiParam.create("get_group_info")
                .param("group_id", group)
                .param("no_cache", noCache));
    }

    /**
     * 获取群列表
     */
    public static void getGroupList() {
        JSONObject res = MainKt.request(ApiParam.create("get_group_list"));
    }

    /**
     * 获取群成员信息
     */
    public static void getGroupMemberInfo(long group, long qq, boolean noCache) {
        JSONObject res = MainKt.request(ApiParam.create("get_group_member_info")
                .param("group_id", group)
                .param("user_id", qq)
                .param("no_cache", noCache));
    }

    /**
     * 获取群成员列表
     */
    public static void getGroupMemberList(long group) {
        JSONObject res = MainKt.request(ApiParam.create("get_group_member_list")
                .param("group_id", group));
    }

    /**
     * 获取群荣誉信息
     */
    public static void getGroupHonorInfo(long group, String type) {
        JSONObject res = MainKt.request(ApiParam.create("get_group_honor_info")
                .param("group_id", group)
                .param("type", type));
    }

    /**
     * 获取 Cookies
     */
    public static void getCookies(String domain) {
        JSONObject res = MainKt.request(ApiParam.create("get_cookies")
                .param("domain", domain));
    }

    /**
     * 获取 CSRF Token
     */
    public static void getCsrfToken() {
        JSONObject res = MainKt.request(ApiParam.create("get_csrf_token"));
    }

    /**
     * 获取 QQ 相关接口凭证
     */
    public static void getCredentials(String domain) {
        JSONObject res = MainKt.request(ApiParam.create("get_credentials")
                .param("domain", domain));
    }

    /**
     * 获取语音
     */
    public static void getRecord(String file, String format) {
        JSONObject res = MainKt.request(ApiParam.create("get_record")
                .param("file", file)
                .param("out_format", format));
    }

    /**
     * 获取图片
     */
    public static void getImage(String file) {
        JSONObject res = MainKt.request(ApiParam.create("get_image")
                .param("file", file));
    }

    /**
     * 检查是否可以发送图片
     */
    public static void canSendImage() {
        JSONObject res = MainKt.request(ApiParam.create("can_send_image"));
    }

    /**
     * 检查是否可以发送语音
     */
    public static void canSendRecord() {
        JSONObject res = MainKt.request(ApiParam.create("can_send_record"));
    }

    /**
     * 获取运行状态
     */
    public static void getStatus() {
        JSONObject res = MainKt.request(ApiParam.create("get_status"));
    }

    /**
     * 获取版本信息
     */
    public static void getVersionInfo() {
        JSONObject res = MainKt.request(ApiParam.create("get_version_info"));
    }

    /**
     * 重启 OneBot 实现
     */
    public static void setRestart() {
        JSONObject res = MainKt.request(ApiParam.create("set_restart"));
    }

    /**
     * 清理缓存
     */
    public static void cleanCache() {
        JSONObject res = MainKt.request(ApiParam.create("clean_cache"));
    }
}
