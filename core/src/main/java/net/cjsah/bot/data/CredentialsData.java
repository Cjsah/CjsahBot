package net.cjsah.bot.data;

import lombok.Data;

@Data
public class CredentialsData {
    private final String cookies;
    private final int csrfToken;
}
