package net.cjsah.bot.data;

import com.alibaba.fastjson2.JSONObject;

public class FileInfo {
    private final String id;
    private final String name;
    private final long size;
    private final long busid;

    public FileInfo(JSONObject raw) {
        this.id = raw.getString("id");
        this.name = raw.getString("name");
        this.size = raw.getLongValue("size");
        this.busid = raw.getLongValue("busid");
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public long getSize() {
        return this.size;
    }

    public long getBusid() {
        return this.busid;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", busid=" + busid +
                '}';
    }
}
