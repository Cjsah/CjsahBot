package net.cjsah.bot.data;

import lombok.Data;

@Data
public class StatusData {
    private boolean online;
    private boolean good;
    private boolean appInitialized;
    private boolean appEnabled;
    private boolean appGood;
}
