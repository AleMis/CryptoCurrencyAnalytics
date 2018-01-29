package crypto_analytics.domain.dbupdater;

import lombok.*;

import java.sql.Time;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DbUpdaterDto {

        private Long id;
        private String currencyPair;
        private String timeFrame;
        private Boolean isDownload;
        private Long startTimestampForFirstDownload;
        private Long endTimestampForFirstDownload;
        private LocalDate updateDate;
        private Time updateTime;
        private Long updateTimestamp;
}