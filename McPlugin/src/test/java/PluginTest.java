import net.cjsah.bot.command.source.ConsoleCommandSource;
import net.cjsah.bot.ext.CrawlerPlugin;
import org.junit.jupiter.api.Test;

public class PluginTest {
    @Test
    public void test() {
//        MCServerStatus.getServerStatus("server.cjsah.net", null);

        CrawlerPlugin.mcbug(297932, new ConsoleCommandSource());

    }
}
