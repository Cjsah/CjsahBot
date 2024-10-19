import net.cjsah.bot.api.Api;
import net.cjsah.bot.data.RoleInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TempTest {

    @Test
    public void test() {
        Api.setToken("");
        String roomId = "3595194642503450624";
        List<RoleInfo> roles = Api.getRoomRoles(roomId);
        System.out.println(roles);
    }
}
