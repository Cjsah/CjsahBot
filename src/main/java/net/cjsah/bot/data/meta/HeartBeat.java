package net.cjsah.bot.data.meta;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cjsah.bot.data.BaseData;
import net.cjsah.bot.data.StatusData;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HeartBeat extends BaseData {
    private long interval;
    private StatusData status;
}
