package net.cjsah.bot.msg;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.msg.nodes.*;

import java.util.function.Function;
import java.util.function.Supplier;

public enum MessageNodeType {
    TEXT(TextMessageNode::new),             // 纯文本
    FACE(FaceMessageNode::new),             // QQ 表情
    IMAGE(ImageMessageNode::new),           // 图片
    RECORD(RecordMessageNode::new),         // 语音
    VIDEO(VideoMessageNode::new),           // 短视频
    AT(AtMessageNode::new),                 // at
    RPS(RpsMessageNode::new),               // 猜拳
    DICE(DiceMessageNode::new),             // 掷骰子
    SHAKE(ShakeMessageNode::new),           // 窗口抖动
    POKE(PokeMessageNode::new),             // 戳一戳
    ANONYMOUS(AnonymousMessageNode::new),   // 匿名消息
    SHARE(ShakeMessageNode::new),           // 链接分享
    CONTACT(ContactMessageNode::new),       // 推荐好友/群
    LOCATION(LocationMessageNode::new),     // 位置
    MUSIC(MusicMessageNode::new),           // 音乐分享
    REPLY(ReplyMessageNode::new),           // 回复
    FORWARD(ForwardMessageNode::new),       // 合并转发
    NODE(NodeMessageNode::new),             // 合并转发节点
    XML(XMLMessageNode::new),               // XML消息
    JSON(json -> new JsonMessageNode(json, true)), // json消息
    ;

    MessageNodeType(Function<JSONObject, MessageNode> factory) {
        this.value = this.name().toLowerCase();
        this.factory = factory;
    }

    MessageNodeType(Supplier<MessageNode> factory) {
        this.value = this.name().toLowerCase();
        this.factory = json -> factory.get();
    }

    private final String value;
    private final Function<JSONObject, MessageNode> factory;

    public String getValue() {
        return this.value;
    }

    public Function<JSONObject, MessageNode> getFactory() {
        return this.factory;
    }
}
