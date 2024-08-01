package net.cjsah.bot.api;

import cn.hutool.core.util.IdUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiParam {
    private final String action;
    private final String echo;
    private final Map<String, Object> params = new HashMap<>();

    public static ApiParam create(String action) {
        return new ApiParam(action, IdUtil.fastSimpleUUID());
    }

    public ApiParam param(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    public String generate() {
        return JsonUtil.serialize(this);
    }
}
