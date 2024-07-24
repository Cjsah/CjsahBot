import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.cjsah.bot.data.Message;
import net.cjsah.bot.data.enums.MessageSource;
import net.cjsah.bot.util.JsonUtil;
import net.cjsah.bot.util.StringUtil;
import org.junit.jupiter.api.Test;

public class JsonTest {

    @Test
    public void run() {
//        String json = "{\"message_type\":\"private\",\"message_id\":1,\"real_id\":1,\"time\":1,\"sender\":1}";
//        Message msg = JsonUtil.deserialize(json, Message.class);
//        System.out.println(msg);
//        String result = JsonUtil.serialize(msg);
//        System.out.println(result);
//
//        TestBean bean = JsonUtil.deserialize("{\"test\":\"private\"}", TestBean.class);
//        System.out.println(bean);

        String a = "ascbaisfchauis&amp;&#91;aaa&#99;";

        System.out.println(StringUtil.netReplace(a));


    }

//    @Data
//    @RequiredArgsConstructor
//    static class TestBean {
//        private final MessageSource test;
//    }
}
