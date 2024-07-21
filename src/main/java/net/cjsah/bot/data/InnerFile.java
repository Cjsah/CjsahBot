package net.cjsah.bot.data;

import lombok.Data;

@Data
public class InnerFile {
    private final String id;
    private final String name;
    private final long size;
    private final long busid;
}
