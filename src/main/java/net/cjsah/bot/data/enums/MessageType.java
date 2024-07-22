package net.cjsah.bot.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageType {
    TEXT,       // 纯文本
    FACE,       // QQ 表情
    IMAGE,      // 图片
    RECORD,     // 语音
    VIDEO,      // 短视频
    AT,         // at
    RPS,        // 猜拳
    DICE,       // 掷骰子
    SHAKE,      // 窗口抖动
    POKE,       // 戳一戳
    ANONYMOUS,  // 匿名消息
    SHARE,      // 链接分享
    CONTACT,    // 推荐好友/群
    LOCATION,   // 位置
    MUSIC,      // 音乐分享
    REPLY,      // 回复
    FORWARD,    // 合并转发
    NODE,       // 合并转发节点
    XML,        // XML消息
    JSON,       // json消息
    ;

    MessageType() {
        this.value = this.name().toLowerCase();
    }

    private final String value;

}
