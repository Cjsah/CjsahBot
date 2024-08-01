package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import net.cjsah.bot.util.JsonUtil;

import java.util.List;

@Data
public class GroupUserHonorData {
    private final long groupId;
    private final HonorData currentTalkative;
    private final List<HonorData> talkativeList;
    private final List<HonorData> performerList;
    private final List<HonorData> legendList;
    private final List<HonorData> strongNewbieList;
    private final List<HonorData> emotionList;

    public static GroupUserHonorData fromJson(JSONObject json) {
        return new GroupUserHonorData(
                json.getLongValue("group_id"),
                JsonUtil.getObject(json, "current_talkative", HonorData.class),
                JsonUtil.getList(json, "talkative_list", HonorData.class),
                JsonUtil.getList(json, "performer_list", HonorData.class),
                JsonUtil.getList(json, "legend_list", HonorData.class),
                JsonUtil.getList(json, "strong_newbie_list", HonorData.class),
                JsonUtil.getList(json, "emotion_list", HonorData.class)
        );
    }
}
