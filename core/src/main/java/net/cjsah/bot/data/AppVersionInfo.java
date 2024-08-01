package net.cjsah.bot.data;

import lombok.Data;

@Data
public class AppVersionInfo {
    private final String appName;
    private final String appVersion;
    private final String protocolVersion;
    private final String ntProtocol;
}
