package net.cjsah.bot.data;

import lombok.Data;

@Data
public class Callback<T> {
    private final String status;
    private final int retcode;
    private final T data;
    private final String echo;
}
