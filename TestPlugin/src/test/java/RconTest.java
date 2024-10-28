import net.cjsah.bot.ext.RconClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RconTest {

    @Test
    public void test() throws IOException {
        RconClient rconClient = new RconClient("", 0);
        byte[] pwd = "pws".getBytes(StandardCharsets.US_ASCII);

        String res = rconClient.request(3, pwd);
        System.out.println(res);
        String list = rconClient.command("list");
        System.out.println(list);

    }
}
