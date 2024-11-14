package net.cjsah.bot.api.card.modules;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.api.card.AbstractCardModule;
import net.cjsah.bot.data.CountdownMode;
import net.cjsah.bot.exception.BuiltExceptions;

public class CountdownCardModule extends AbstractCardModule {
    private static final long day99 = 8553600L;
    private final CountdownMode mode;
    private final long time;

    public CountdownCardModule(CountdownMode mode, long time) {
        super("countdown");
        long current = System.currentTimeMillis() / 1000;
        long after99 = current + day99;
        if (time < current) {
            throw BuiltExceptions.COUNTDOWN_LESS_NOW.create(current);
        }
        if (time > after99) {
            throw BuiltExceptions.COUNTDOWN_OVER_COUNT.create(after99);
        }
        this.mode = mode;
        this.time = time;
    }

    @Override
    public JSONObject generate() {
        JSONObject result = super.generate();
        result.put("mode", this.mode.getValue());
        result.put("end_time", this.time);
        return result;
    }
}
