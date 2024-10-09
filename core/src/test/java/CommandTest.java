import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

public class CommandTest {
    @Test
    public void test() {
        int code = 1;
        HttpRequest request = HttpRequest.get("https://bugs.mojang.com/rest/api/2/issue/MC-" + code);
        try (HttpResponse response = request.execute()) {
            if (response.getStatus() != 200) {
                System.out.println("获取mc bug信息失败: " + response.getStatus());
                return;
            }
            String body = response.body();
            JSONObject fields = JsonUtil.deserialize(body).getJSONObject("fields");
            String versions = fields.getList("versions", JSONObject.class)
                    .stream()
                    .map(it -> it.getString("name")
                            .replace("Minecraft ", "")
                            .replace("Pre-Release ", "Pre-")
                            .replace("Release Candidate ", "RC-"))
                    .collect(Collectors.joining(" "));

            String val = "https://bugs.mojang.com/browse/MC-%d%n%s%n类型: %s%n状态: %s%n解决结果: %s%n报告人: %s%n影响版本: %s%n%s".formatted(
                    code,
                    fields.getString("summary"),
                    getName(fields, "issuetype", "未知"),
                    getName(fields, "status", "未知"),
                    getName(fields, "resolution", "未解决"),
                    getName(fields, "reporter", "未知"),
                    versions,
                    fields.getString("description").replace("\r\n\r\n", "\n")
            );
            System.out.println(val);
        }
    }

    private static String getName(JSONObject json, String key, String defaultValue) {
        JSONObject node = json.getJSONObject(key);
        if (node == null) return defaultValue;
        return node.getString("name");
    }

}
