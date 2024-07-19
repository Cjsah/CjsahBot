package net.cjsah.bot.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StatusData {
    private boolean online;
    private boolean good;
    @JsonProperty("app_initialized")
    private boolean appInitialized;
    @JsonProperty("app_enabled")
    private boolean appEnabled;
    @JsonProperty("app_good")
    private boolean appGood;
}
