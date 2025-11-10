package util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
    // VN timezone mặc định
    public static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDate today() { return LocalDate.now(ZONE); }
    public static LocalTime nowTime() { return LocalTime.now(ZONE); }
    public static LocalDate parseDate(String s) { return LocalDate.parse(s, DATE_FMT); }

    public static boolean before8AMToday() {
        LocalTime now = nowTime();
        return now.isBefore(LocalTime.of(8,0));
    }

    // convert java.util.Date <-> java.time if cần
    public static Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZONE).toInstant());
    }
}
