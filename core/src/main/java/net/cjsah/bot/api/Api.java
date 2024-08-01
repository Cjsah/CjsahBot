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
     * <p>
     * 该方法通过调用API接口发送私聊消息，是机器人与用户进行私聊的重要方式
     *
     * @param qq      发送消息的目标用户的QQ号码
     * @param message 要发送的消息链，包含了消息的内容和类型
     * @return 返回发送的消息的ID，用于后续可能需要对消息进行操作（如撤回）
     */
    public static int sendPrivateMsg(long qq, MessageChain message) {
        // 构建API请求参数，并发送请求
        Object res = MainKt.request(ApiParam.create("send_private_msg")
            .param("user_id", qq)
            .param("message", message.toJson()));
        // 解析返回结果，提取并返回消息ID
        return ((JSONObject) res).getIntValue("message_id");
    }

    /**
     * 发送群消息
     * <p>
     * 此方法通过调用API接口发送群消息，使用了主函数中的请求方法，并将群号和消息内容作为参数传入
     * 通过将消息转换为JSON格式，便于网络传输和服务器处理
     *
     * @param group   群号，表示要发送消息的目标群
     * @param message 消息链，表示要发送的具体消息内容
     * @return 返回发送的消息的ID，以便后续可能的引用或查询
     */
    public static int sendGroupMsg(long group, MessageChain message) {
        Object res = MainKt.request(ApiParam.create("send_group_msg")
            .param("group_id", group)
            .param("message", message.toJson()));
        return ((JSONObject) res).getIntValue("message_id");
    }

    /**
     * 撤回消息
     * <p>
     * 该方法通过发送API请求来撤回指定的消息它使用了ApiParam创建对象来构建请求参数，
     * 并通过MainKt.request方法发送请求，以实现消息的撤回功能
     *
     * @param messageId 消息ID，用于指定需要撤回的消息
     */
    public static void RecallMsg(int messageId) {
        // 构建撤回消息的API请求参数，并发送请求
        MainKt.request(ApiParam.create("delete_msg")
            .param("message_id", messageId), false);
    }

    /**
     * 根据消息ID获取消息详情
     * <p>
     * 此方法通过调用后端服务的API来请求特定消息的详细信息返回的消息数据经过JSON解析，
     * 转换为Message对象，以便于前端使用和操作
     *
     * @param messageId 消息ID，用于指定需要获取哪条消息
     * @return 返回获取到的消息对象，包含详细的消息内容和相关信息
     */
    public static Message getMsg(int messageId) {
        // 调用MainKt中的request方法，传入构建的ApiParam对象和消息ID，发起网络请求
        Object res = MainKt.request(ApiParam.create("get_msg")
            .param("message_id", messageId));
        // 将请求结果转换为Message对象并返回
        return JsonUtil.convert(res, Message.class);
    }

    /**
     * 获取合并转发消息
     * <p>
     * 本函数通过请求API接口来获取指定id的合并转发消息详情
     * 它首先构建API请求参数，然后发送请求，最后将响应结果转换为MessageChain对象返回
     *
     * @param id 转发消息的唯一标识符，用于指定要获取的合并转发消息
     * @return 返回一个MessageChain对象，包含合并转发消息的内容
     */
    public static MessageChain getForwardMsg(String id) {
        // 构建并发送API请求参数，获取合并转发消息
        Object res = MainKt.request(ApiParam.create("get_forward_msg")
            .param("id", id));
        // 将响应结果转换为MessageChain对象并返回
        return JsonUtil.convert(res, MessageChain.class);
    }

    /**
     * 发送好友赞
     * <p>
     * 说明：
     * 该方法用于向指定好友发送点赞，通过调用MainKt中的request方法实现。
     * 用ApiParam.create方法构建请求参数，包括用户ID(user_id)和点赞次数(times)。
     *
     * @param qq    好友的QQ号码
     * @param count 点赞次数
     */
    public static void sendLike(long qq, int count) {
        MainKt.request(ApiParam.create("send_like")
            .param("user_id", qq)
            .param("times", count), false);
    }

    /**
     * 群组踢人
     * <p>
     * 此方法用于将指定QQ号的用户从群组中移除，不支持延时踢人功能
     *
     * @param group 群组ID，标识需要操作的群组
     * @param qq    需要被移除的用户的QQ号
     */
    public static void groupKickUser(long group, long qq) {
        groupKickUser(group, qq, false);
    }

    /**
     * 群组踢人
     *
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
     * <p>
     * 该方法用于在指定群组中禁言某个成员一定时长通过调用后台API接口实现禁言功能
     *
     * @param group    群号，指定成员所属的群组ID
     * @param qq       成员QQ号，需要被禁言的成员ID
     * @param duration 禁言时长，单位为秒，表示成员被禁言的持续时间
     */
    public static void groupMuteUser(long group, long qq, int duration) {
        // 构建API参数并发送禁言请求
        MainKt.request(ApiParam.create("set_group_ban")
            .param("group_id", group)
            .param("user_id", qq)
            .param("duration", duration), false);
    }

    /**
     * 群成员解除禁言
     * <p>
     * 该方法通过调用groupMuteUser方法实现群成员的解除禁言操作，将禁言时长设置为0达到解除禁言的效果
     *
     * @param group 群号，指定需要解除禁言的群
     * @param qq    用户的QQ号，指定需要解除禁言的用户
     */
    public static void groupCancelMuteUser(long group, long qq) {
        groupMuteUser(group, qq, 0);
    }

    /**
     * 群组匿名用户禁言
     * <p>
     * 此方法用于在指定群组中对匿名用户实施禁言操作
     *
     * @param group    群组ID，标识哪个群组
     * @param flag     匿名用户的标识符，用于确定是哪个匿名用户
     * @param duration 禁言时长，单位为秒，用于设置匿名用户被禁言的时间
     */
    public static void groupMuteAnonymous(long group, String flag, int duration) {
        // 构建API调用参数，并发起请求，以实现对匿名用户的禁言操作
        MainKt.request(ApiParam.create("set_group_anonymous_ban")
            .param("group_id", group)
            .param("flag", flag)
            .param("duration", duration), false);
    }

    /**
     * 群组全员禁言
     * <p>
     * 该方法用于在指定的群组中设置全员禁言状态
     *
     * @param group 群组ID，用于指定需要设置全员禁言的群组
     * @param mute  是否启用全员禁言，true为启用，false为关闭
     */
    public static void groupMute(long group, boolean mute) {
        // 构建请求参数并发送请求，以设置群组的全员禁言状态
        MainKt.request(ApiParam.create("set_group_whole_ban")
            .param("group_id", group)
            .param("enable", mute), false);
    }

    /**
     * 群组设置管理员
     * <p>
     * 该方法通过调用API来设置或取消用户的管理员身份，根据传入的群组ID和用户QQ号码
     * 参数"enable"决定了是设置还是取消用户管理员身份
     *
     * @param group 群组ID，标识特定的群组
     * @param qq    用户的QQ号码，标识特定的用户
     * @param set   布尔值，true表示设置管理员，false表示取消管理员
     */
    public static void setGroupAdmin(long group, long qq, boolean set) {
        MainKt.request(ApiParam.create("set_group_admin")
            .param("group_id", group)
            .param("user_id", qq)
            .param("enable", set), false);
    }

    /**
     * 设置群组是否允许匿名聊天
     *
     * @param group  群组ID，用于指定需要设置的群组
     * @param enable 是否启用匿名聊天功能，true表示启用，false表示禁用
     */
    public static void setGroupAnonymous(long group, boolean enable) {
        // 构建API请求参数，并发起请求，无需等待响应
        MainKt.request(ApiParam.create("set_group_anonymous")
            .param("group_id", group)
            .param("enable", enable), false);
    }

    /**
     * 设置群名片（群备注）
     *
     * @param group 群号，用于指定需要设置名片的群
     * @param qq    用户的QQ号，用于指定需要设置名片的用户
     * @param card  新的群名片（备注名），用于更新用户在群内的名片
     */
    public static void setGroupCard(long group, long qq, String card) {
        // 构建设置群名片的API请求参数
        MainKt.request(ApiParam.create("set_group_card")
            .param("group_id", group)
            .param("user_id", qq)
            .param("card", card));
    }

    /**
     * 设置群名
     *
     * @param group 群ID，用于指定要修改群名的群
     * @param qq    机器人QQ号，用于识别机器人身份
     * @param name  新的群名，用于更新群的名称
     */
    public static void setGroupName(long group, long qq, String name) {
        // 调用MainKt中的request方法，请求修改群名称
        MainKt.request(ApiParam.create("set_group_name")
            .param("group_id", group)
            .param("group_name", name), false);
    }

    /**
     * 退出群组
     *
     * @param group 群组ID，用于标识特定的群组
     */
    public static void setGroupLeave(long group) {
        setGroupLeave(group, false);
    }

    /**
     * 退出群组
     *
     * @param group   群组ID，用于标识特定的群组
     * @param dismiss 是否解散该群，仅群主可用。如果为true，表示解散群组；否则，表示仅退出群组。
     */
    public static void setGroupLeave(long group, boolean dismiss) {
        // 构建请求参数并发送请求，以执行退出或解散群组的操作
        MainKt.request(ApiParam.create("set_group_leave")
            .param("group_id", group)
            .param("is_dismiss", dismiss), false);
    }

    /**
     * 设置群组专属头衔
     *
     * @param group 群组ID，用于指定在哪个群组中设置专属头衔
     * @param qq    用户的QQ号码，用于指定为哪个用户设置专属头衔
     * @param title 专属头衔的名称，用于指定设置的具体头衔内容
     */
    public static void setGroupSpecialTitle(long group, long qq, String title) {
        // 构建设置群组专属头衔的API请求参数，并发送请求
        MainKt.request(ApiParam.create("set_group_special_title")
            .param("group_id", group)
            .param("user_id", qq)
            .param("special_title", title), false);
    }

    /**
     * 处理加好友请求
     * <p>
     * 该方法用于向服务器发送处理加好友请求的指令，支持接受或拒绝好友请求，并可附加备注信息
     *
     * @param flag    好友请求的标识，用于识别特定的好友请求
     * @param approve 是否接受好友请求，true表示接受，false表示拒绝
     * @param remark  处理该好友请求时附加的备注信息，可用于记录处理原因或备忘信息
     */
    public static void setFriendAddRequest(String flag, boolean approve, String remark) {
        // 构建并发送处理加好友请求的API参数
        MainKt.request(ApiParam.create("set_friend_add_request")
            .param("flag", flag)
            .param("approve", approve)
            .param("remark", remark), false);
    }

    /**
     * 处理加群请求／邀请
     * <p>
     * 本函数用于处理接收到的加群请求或邀请。通过指定的参数，可以同意或拒绝请求，并提供相应的原因。
     *
     * @param flag    从请求／邀请事件中获取的标志，用于标识特定的请求／邀请事件。
     * @param type    请求／邀请的类型，如"add"表示加群请求，"invite"表示入群邀请。
     * @param approve 是否同意请求／邀请。如果为true，则同意；如果为false，则拒绝。
     * @param reason  操作者给定的原因，可以是同意或拒绝的解释。
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
     * <p>
     * 本函数通过调用MainKt中的request函数，使用"get_login_info"参数请求登录号信息
     * 返回的Object类型结果通过JsonUtil的convert方法转换为UserBaseData对象
     *
     * @return 返回UserBaseData类型的登录号信息
     */
    public static UserBaseData getLoginInfo() {
        Object res = MainKt.request(ApiParam.create("get_login_info"));
        return JsonUtil.convert(res, UserBaseData.class);
    }

    /**
     * 获取陌生人信息
     * <p>
     * 此方法通过发送请求来获取指定QQ号的用户信息，可以选择是否跳过缓存
     * 主要用于在用户首次使用或在缓存失效时，获取最新的用户数据
     *
     * @param qq      需要获取信息的用户QQ号
     * @param noCache 是否忽略缓存，true为忽略缓存，false为允许使用缓存
     * @return UserData 返回用户的详细信息数据
     */
    public static UserData getStrangerInfo(long qq, boolean noCache) {
        // 构建并发送请求，获取陌生人信息
        Object res = MainKt.request(ApiParam.create("get_stranger_info")
            .param("user_id", qq)
            .param("no_cache", noCache));
        // 将获取到的信息转换为UserData对象并返回
        return JsonUtil.convert(res, UserData.class);
    }

    /**
     * 获取好友列表
     * <p>
     * 本函数通过调用MainKt中的request函数，使用"get_friend_list"参数获取好友列表数据
     * 返回的数据经过JsonUtil的处理，转换为List<FriendData>类型，以便后续使用
     *
     * @return 返回一个包含所有好友信息的列表，列表中的每个元素都是FriendData类型的对象
     */
    public static List<FriendData> getFriendList() {
        // 调用MainKt的request方法，传入ApiParam创建的参数对象，用于请求好友列表数据
        Object res = MainKt.request(ApiParam.create("get_friend_list"));
        // 使用JsonUtil将返回的结果转换为FriendData类型的列表
        return JsonUtil.convertList(res, FriendData.class);
    }

    /**
     * 获取群信息
     * <p>
     * 此方法通过调用API接口来获取指定群的信息，如果noCache参数为true，则不使用缓存
     *
     * @param group   群号，用于指定要获取信息的群
     * @param noCache 是否不使用缓存，true为不使用缓存，false为使用缓存
     * @return 返回群信息的GroupData对象
     */
    public static GroupData getGroupInfo(long group, boolean noCache) {
        // 创建并设置API参数
        Object res = MainKt.request(ApiParam.create("get_group_info")
            .param("group_id", group)
            .param("no_cache", noCache));
        // 将返回的结果转换为GroupData对象
        return JsonUtil.convert(res, GroupData.class);
    }

    /**
     * 获取群列表
     * <p>
     * 本函数通过调用后台API接口，获取当前用户的群组列表
     * 使用MainKt中的request方法发送网络请求，并通过JsonUtil将返回的JSON数据转换为GroupData对象列表
     *
     * @return 返回一个List对象，包含用户所有的群组数据
     */
    public static List<GroupData> getGroupList() {
        // 调用MainKt的request方法，传入ApiParam创建的参数对象，用于请求群列表数据
        Object res = MainKt.request(ApiParam.create("get_group_list"));
        // 将请求返回的结果转换为GroupData类型的列表
        return JsonUtil.convertList(res, GroupData.class);
    }

    /**
     * 获取群成员信息
     * <p>
     * 此方法通过调用API接口来获取指定群内特定成员的详细信息
     *
     * @param group   群号，标识特定的群组
     * @param qq      成员的QQ号，用于识别特定的群成员
     * @param noCache 是否强制不使用缓存，确保获取最新数据
     * @return 返回群成员信息的对象，包含各种详细属性
     */
    public static GroupUserData getGroupMemberInfo(long group, long qq, boolean noCache) {
        // 构建并发送API请求，请求特定群成员的详细信息
        Object res = MainKt.request(ApiParam.create("get_group_member_info")
            .param("group_id", group)
            .param("user_id", qq)
            .param("no_cache", noCache));
        // 将获取到的信息转换为GroupUserData对象，以便使用
        return JsonUtil.convert(res, GroupUserData.class);
    }

    /**
     * 获取群成员列表
     * <p>
     * 该方法通过调用接口请求来获取指定群的成员列表，并将响应结果转换为群成员数据列表
     *
     * @param group 群ID，用于指定要获取成员列表的群
     * @return 返回群成员列表，列表中的每个元素代表一名群成员
     */
    public static List<GroupUserData> getGroupMemberList(long group) {
        // 创建并发起接口请求，请求获取指定群的成员列表
        Object res = MainKt.request(ApiParam.create("get_group_member_list")
            .param("group_id", group));
        // 将请求响应结果转换为群成员数据列表并返回
        return JsonUtil.convertList(res, GroupUserData.class);
    }

    /**
     * 获取群荣誉信息
     * <p>
     * 此方法用于查询指定群的荣誉信息，包括群成员的荣誉数据和荣誉类型
     *
     * @param group 群ID，用于指定要获取荣誉信息的群
     * @return 返回群荣誉信息的实例，包含群成员的荣誉数据和荣誉类型
     */
    public static GroupUserHonorData getGroupHonorInfo(long group) {
        // 发起网络请求，获取群荣誉信息
        Object res = MainKt.request(ApiParam.create("get_group_honor_info")
            .param("group_id", group)
            .param("type", "all"));
        // 将返回的JSON对象解析为群荣誉数据对象
        return GroupUserHonorData.fromJson((JSONObject) res);
    }

    /**
     * 获取 QQ 相关接口凭证
     * <p>
     * 此方法通过发送请求到指定域名的 API 接口，以获取 QQ 相关接口的凭证信息
     *
     * @param domain 域名，用于指定请求的环境
     * @return CredentialsData 包含 QQ 接口凭证的数据对象
     */
    public static CredentialsData getCredentials(String domain) {
        // 发起请求获取凭证信息
        Object res = MainKt.request(ApiParam.create("get_credentials")
            .param("domain", domain));
        // 将请求结果转换为 CredentialsData 对象并返回
        return JsonUtil.convert(res, CredentialsData.class);
    }

    /**
     * 检查是否可以发送图片
     * <p>
     * 该方法通过调用后端服务的接口来判断当前用户是否允许发送图片
     * 它不直接操作UI，而是通过发送请求来获取状态，并根据返回的结果来判断是否可以发送图片
     *
     * @return 返回一个布尔值，表示是否允许发送图片
     */
    public static boolean canSendImage() {
        // 调用request方法，请求后端服务，判断是否可以发送图片
        Object res = MainKt.request(ApiParam.create("can_send_image"));
        // 从返回的结果中提取布尔值"yes"，以判断是否可以发送图片
        return ((JSONObject) res).getBooleanValue("yes");
    }

    /**
     * 检查是否可以发送语音
     *
     * @return 如果可以发送语音，返回true；否则返回false
     */
    public static boolean canSendRecord() {
        Object res = MainKt.request(ApiParam.create("can_send_record"));
        return ((JSONObject) res).getBooleanValue("yes");
    }

    /**
     * 获取版本信息
     * <p>
     * 该方法通过调用后端接口请求，获取应用程序的版本信息，并将响应结果转换为AppVersionInfo对象
     * 使用了通用的网络请求封装方法，并利用Json库进行类型转换
     */
    public static AppVersionInfo getVersionInfo() {
        // 调用request方法发送网络请求，获取版本信息的响应数据
        Object res = MainKt.request(ApiParam.create("get_version_info"));
        // 将响应数据转换为AppVersionInfo对象并返回
        return JsonUtil.convert(res, AppVersionInfo.class);
    }

    /**
     * 重启 OneBot 实现
     * <p>
     * 该方法通过发送重启请求来重启 OneBot 实现。它使用了 ApiParam.create 方法来创建一个重启请求，
     * 并设置了一个 500 毫秒的延迟参数。这意味着在发出重启指令后，系统将等待 500 毫秒，
     * 以确保所有正在进行的操作完成或释放相关资源。
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
