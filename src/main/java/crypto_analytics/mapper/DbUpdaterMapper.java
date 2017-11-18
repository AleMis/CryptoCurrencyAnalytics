package crypto_analytics.mapper;


import crypto_analytics.domain.dbupdater.DbUpdater;
import crypto_analytics.domain.dbupdater.DbUpdaterDto;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Component
public class DbUpdaterMapper {


    public DbUpdater mapToDbUpdater(DbUpdaterDto dbUpdaterDto) {
        return new DbUpdater(
                dbUpdaterDto.getId(),
                dbUpdaterDto.getCurrencyPair(),
                dbUpdaterDto.getTimeFrame(),
                dbUpdaterDto.getIsDownload(),
                dbUpdaterDto.getStartTimestampForFirstDownload(),
                dbUpdaterDto.getEndTimestampForFirstDownload(),
                dbUpdaterDto.getUpdateDate().toString(),
                dbUpdaterDto.getUpdateTime().toString(),
                dbUpdaterDto.getUpdateTimestamp());
    }

    public DbUpdaterDto mapToDbUpdaterDto(DbUpdater dbUpdater) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dbUpdater.getUpdateDate(), dtf);
        Time time = Time.valueOf(dbUpdater.getUpdateTime());
        return new DbUpdaterDto(
                dbUpdater.getId(),
                dbUpdater.getCurrencyPair(),
                dbUpdater.getTimeFrame(),
                dbUpdater.getIsDownload(),
                dbUpdater.getStartTimestampForFirstDownload(),
                dbUpdater.getEndTimestampForFirstDownload(),
                localDate,
                time,
                dbUpdater.getUpdateTimestamp());
    }
}
