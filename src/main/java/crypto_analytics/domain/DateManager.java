package crypto_analytics.domain;

import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class DateManager {

    public LocalDateTime returnCurrentLocalDateTime(String timeFrame) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime time = null;
        switch (timeFrame) {
            case "1h":
                time = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonthValue(), currentDateTime.getDayOfMonth(), currentDateTime.getHour(), 0, 0, 0);
                break;
            case "1D":
                time = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonthValue(), currentDateTime.getDayOfMonth(), 0, 0, 0, 0);
                break;
        }
        return time;
    }

    public LocalDateTime convertTimestampToLocalDateTime(Long timestamp) {
        Timestamp ts = new Timestamp(timestamp);
        return LocalDateTime.ofInstant(ts.toInstant(), ZoneOffset.ofHours(0));
    }

    public Long convertLocalDateTimeToLong(LocalDateTime localDateTime) {
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return timestamp.getTime();
    }
}
