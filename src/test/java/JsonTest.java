import com.fasterxml.jackson.core.JsonProcessingException;
import net.cjsah.bot.data.notice.GroupAdminChange;
import net.cjsah.bot.util.JsonUtil;
import org.junit.jupiter.api.Test;

public class JsonTest {

    @Test
    public void run() throws JsonProcessingException {
        String json = "{\"sub_type\":\"set\",\"group_id\":1,\"user_id\":1,\"time\":1,\"self_id\":1}";
        GroupAdminChange group = JsonUtil.deserialize(json, GroupAdminChange.class);
        System.out.println(group);
        String result = JsonUtil.serialize(group);
        System.out.println(result);
    }

}
