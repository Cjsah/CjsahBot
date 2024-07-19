package net.cjsah.bot.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HeartBeat extends BaseData {
    private long interval;
    private StatusData status;
}
