package net.cjsah.bot.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public final class DateUtil {
    public static ZonedDateTime nowDate() {
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.atZone(ZoneId.of("GMT+8"));
    }

    public static Date now() {
        return Date.from(nowDate().toInstant());
    }

    public static String format(Date date, String format) {
        SimpleDateFormat sfd = new SimpleDateFormat(format);
        return sfd.format(date);

    }

}
