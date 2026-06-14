package net.cjsah.bot.api.message.nodes;

import net.cjsah.bot.api.message.MessageNodeType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class LocationMessageNode extends MessageNode {
    private final float lat;
    private final float lon;
    private final String title;
    private final String description;

    public LocationMessageNode(float lat, float lon, @Nullable String title, @Nullable String description) {
        super(MessageNodeType.LOCATION);
        this.lat = lat;
        this.lon = lon;
        this.title = title;
        this.description = description;
    }

    public LocationMessageNode(JSONObject json) {
        super(MessageNodeType.LOCATION);
        this.lat = this.parseToFloat(json, "lat");
        this.lon = this.parseToFloat(json, "lon");
        this.title = this.parsetoString(json, "title");
        this.description = this.parsetoString(json, "content");
    }

    @Override
    public void serializeData(JSONObject json) {
        json.put("lat", String.valueOf(this.lat));
        json.put("lon", String.valueOf(this.lon));
        json.put("title", this.title);
        json.put("content", this.description);
    }


    @Override
    public String toString() {
        return this.pair("location", Map.of(
                "lat", this.lat,
                "lon", this.lon,
                "title", this.title,
                "description", this.description
        ));
    }
}
