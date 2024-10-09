import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.bot.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TempTest {
    @Test
    public void test() {
        String ip = "server.cjsah.net";
        int port = 25565;

        JSONObject info = requestInfo(ip, port);
        JSONObject players = info.getJSONObject("players");
        JSONObject description = info.getJSONObject("description");
        String text = description.getString("text");
        if (description.containsKey("extra")) {
            List<JSONObject> extra = description.getList("extra", JSONObject.class);
            text = extra.stream()
                    .map(it -> it.getString("text"))
                    .collect(Collectors.joining("\n", "\n", "\n"));
        }
        text = text.replaceAll("\\u00a7([a-zA-Z0-9])", "");
        String val = """
        %s:%d
        
        服务器版本: %s
        
        在线人数: %d/%d
        
        %s
        """.formatted(
                ip, port,
                info.getJSONObject("version").getString("name"),
                players.getIntValue("online"),
                players.getIntValue("max"),
                text
        );

        System.out.println(val);

    }

    private JSONObject requestInfo(String ip, int port) {

        byte[] request = new RequestInfoGenerator(ip, port).generate();

        try (Socket socket = new Socket(ip, port);
             InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            socket.setSoTimeout(1000);

            out.write(request);
            out.write(new byte[]{1, 0});
            out.flush();

            int length = 0;
            int index = 0;

            while (true) {
                byte b = (byte) in.read();
                length += (b & 0x7F) << (7 * index++);
                if ((b & 0x80) <= 0) {
                    break;
                }
            }

            byte[] array = new byte[length];
            index = 0;

            while (length > 0) {
                byte[] bytes = new byte[length];
                int count = in.read(bytes);
                length -= count;
                for (int i = 0; i < count; i++) {
                    array[index++] = bytes[i];
                }
            }
            String str = new String(deleteHead(array));
            return JsonUtil.deserialize(str);
        } catch (Exception e) {
            log.error("Err", e);
            return new JSONObject();
        }
    }

    private static List<Byte> varInt(int x) {
        List<Byte> array = new ArrayList<>();
        int num = (int) (((long) x) & 0xFFFFFFFFL);
        while ((num & -128) > 0) {
            array.add((byte) (num & 127 | 128));
            num >>= 7;
        }
        array.add((byte) num);
        return array;
    }

    private static byte[] deleteHead(byte[] array) {
        int index = 0;
        while (true) {
            byte slot0 = array[index++];
            if ((slot0 & 0x80) <= 0) {
                break;
            }
        }
        while (true) {
            byte slot0 = array[index++];
            if ((slot0 & 0x80) <= 0) {
                break;
            }
        }
        return Arrays.copyOfRange(array, index, array.length);
    }

    public static class RequestInfoGenerator {
        private final byte[] bytes;
        private int index;

        public RequestInfoGenerator(String ip, int port) {
            byte[] bytes = ip.getBytes();
            List<Byte> length = varInt(bytes.length);
            this.bytes = new byte[bytes.length + length.size() + 5];
            this.index = 0;
            this.write(-108, 3);
            this.write(length);
            this.write(bytes);
            this.write(port / 256, port % 256, 1);
        }

        private void write(List<Byte> array) {
            for (byte b : array) {
                this.bytes[this.index++] = b;
            }
        }

        private void write(byte[] array) {
            for (byte b : array) {
                this.bytes[this.index++] = b;
            }
        }

        private void write(int... bytes) {
            for (int b : bytes) {
                this.bytes[this.index++] = (byte) b;
            }
        }

        public byte[] generate() {
            List<Byte> length = varInt(this.bytes.length + 1);
            byte[] result = new byte[length.size() + this.bytes.length + 1];
            int index = 0;
            for (byte b : length) {
                result[index++] = b;
            }
            result[index++] = 0;
            for (byte b : this.bytes) {
                result[index++] = b;
            }
            return result;
        }
    }
}
