package net.cjsah.bot.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaseData {
    private long time;
    @JsonProperty("self_id")
    private long selfId;
}
