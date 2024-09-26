import net.cjsah.bot.commandV2.Command;
import net.cjsah.bot.commandV2.CommandManager;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class CommandTest {
    @Test
    public void test() {
        CommandManager.register(CommandTest.class);
    }

    @Command("/test <a> <b> <c> <p> ")
    public static void testCmd(int a, boolean b, String c, Map<String, String> p) {
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(p);

    }

}
