package net.cjsah.bot.ext;

import cn.hutool.core.util.IdUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class RconClient {
    private Socket client;

    public RconClient(String hostname, int port) throws IOException {
        this.client = new Socket();
        this.client.setKeepAlive(true);
        this.client.setTrafficClass(0x04);
        this.client.setSendBufferSize(1460);
        this.client.setReceiveBufferSize(4096);
        this.client.connect(new InetSocketAddress(hostname, port));
    }

    private void makeSocket() throws SocketException {
        this.client = new Socket();
        this.client.setKeepAlive(true);
        this.client.setTrafficClass(0x04);
        this.client.setSendBufferSize(1460);
        this.client.setReceiveBufferSize(4096);
    }

    public void connect(String hostname, int port) throws IOException {
        if (this.client != null) {
            if (this.client.isConnected()) {
                throw new IllegalStateException("Already connected");
            } else if (!this.client.isClosed()) {
                this.client.close();
            }
        }
        this.client = new Socket();
    }

    public String command(String payload) throws IOException {
        return this.request(2, payload.getBytes());
    }

    public String request(int type, byte[] payload) throws IOException {
        int length = payload.length + 10;
        int id = IdUtil.fastSimpleUUID().hashCode();
        byte[] out = new byte[length + 4];
        ByteBuffer buf = ByteBuffer.wrap(out);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(length).putInt(id).putInt(type).put(payload).putShort((short)0);
        this.client.getOutputStream().write(out);
        System.out.println("send");
        System.out.println(id);
        System.out.println("===");
        return this.parsePackage();
    }

    private String parsePackage() throws IOException {
        byte[] in = new byte[4096];
        ByteBuffer buf = ByteBuffer.wrap(in);
        buf.order(ByteOrder.LITTLE_ENDIAN);

        int bytesRead = 0;
        while (bytesRead < 12) {
            buf.position(bytesRead);
            bytesRead += client.getInputStream().read(in, bytesRead, buf.remaining());
        }
        buf.rewind();

        int length = buf.getInt();
        int id = buf.getInt();
        int type = buf.getInt();
        // Length includes requestId, type, and the two null bytes.
        // Subtract 10 to ignore those values from the payload size.
        byte[] payload = new byte[length - 10];

        buf.mark();
        while (bytesRead - 12 < payload.length + 2) {
            buf.position(bytesRead);
            bytesRead += client.getInputStream().read(in, bytesRead, buf.remaining());
        }
        buf.reset();
        buf.get(payload);
        System.out.println("receive");
        System.out.println(id);
        System.out.println(type);
        return new String(payload, StandardCharsets.US_ASCII);
    }
}
