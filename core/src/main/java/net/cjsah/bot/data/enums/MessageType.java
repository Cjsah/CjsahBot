package net.cjsah.bot.data.enums;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.msg.MessageNode;
import net.cjsah.bot.msg.nodes.AnonymousMessageNode;
import net.cjsah.bot.msg.nodes.AtMessageNode;
import net.cjsah.bot.msg.nodes.ContactMessageNode;
import net.cjsah.bot.msg.nodes.DiceMessageNode;
import net.cjsah.bot.msg.nodes.FaceMessageNode;
import net.cjsah.bot.msg.nodes.ForwardMessageNode;
import net.cjsah.bot.msg.nodes.ImageMessageNode;
import net.cjsah.bot.msg.nodes.JsonMessageNode;
import net.cjsah.bot.msg.nodes.LocationMessageNode;
import net.cjsah.bot.msg.nodes.MusicMessageNode;
import net.cjsah.bot.msg.nodes.NodeMessageNode;
import net.cjsah.bot.msg.nodes.PokeMessageNode;
import net.cjsah.bot.msg.nodes.RecordMessageNode;
import net.cjsah.bot.msg.nodes.ReplyMessageNode;
import net.cjsah.bot.msg.nodes.RpsMessageNode;
import net.cjsah.bot.msg.nodes.ShakeMessageNode;
import net.cjsah.bot.msg.nodes.TextMessageNode;
import net.cjsah.bot.msg.nodes.VideoMessageNode;
import net.cjsah.bot.msg.nodes.XMLMessageNode;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum MessageType {
    TEXT(TextMessageNode::new),                         // 纯文本
    FACE(FaceMessageNode::new),                         // QQ 表情
    IMAGE(ImageMessageNode::new),                       // 图片
    RECORD(RecordMessageNode::new),                     // 语音
    VIDEO(VideoMessageNode::new),                       // 短视频
    AT(AtMessageNode::new),                             // at
    RPS(json -> new RpsMessageNode()),                  // 猜拳
    DICE(json -> new DiceMessageNode()),                // 掷骰子
    SHAKE(json -> new ShakeMessageNode()),              // 窗口抖动
    POKE(PokeMessageNode::new),                         // 戳一戳
    ANONYMOUS(json -> new AnonymousMessageNode()),      // 匿名消息
    SHARE(json -> new ShakeMessageNode()),              // 链接分享
    CONTACT(ContactMessageNode::new),                   // 推荐好友/群
    LOCATION(LocationMessageNode::new),                 // 位置
    MUSIC(MusicMessageNode::new),                       // 音乐分享
    REPLY(ReplyMessageNode::new),                       // 回复
    FORWARD(ForwardMessageNode::new),                   // 合并转发
    NODE(NodeMessageNode::new),                         // 合并转发节点
    XML(XMLMessageNode::new),                           // XML消息
    JSON(json -> new JsonMessageNode(json, true)), // json消息
    ;

    MessageType(Function<JSONObject, MessageNode> factory) {
        this.value = this.name().toLowerCase();
        this.factory = factory;
    }

    private final String value;
    private final Function<JSONObject, MessageNode> factory;

}
