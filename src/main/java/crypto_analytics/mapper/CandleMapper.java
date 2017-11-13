package crypto_analytics.mapper;

import crypto_analytics.domain.candle.Candle;
import crypto_analytics.domain.candle.CandleDto;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CandleMapper {

    public List<CandleDto> mapToCandletDtoArrays(List<Object[][]> twoDimObjectsList) {
        List<CandleDto> candleDtoList = new ArrayList<>();
        for(Object[][] object : twoDimObjectsList) {
            CandleDto[] candleArray = new CandleDto[object.length];
            Long timeStamp = null;
            Double open = null;
            Double close = null;
            Double high = null;
            Double low = null;
            Double volume = null;

            for (int i = 0; i < object.length; i++) {
                CandleDto candleDto = null;
                for (int j = 0; j < object[i].length; j++) {
                    if (j == 0) {
                        timeStamp = Long.valueOf(object[i][j].toString());
                    } else if (j == 1) {
                        open = Double.valueOf(object[i][j].toString());
                    } else if (j == 2) {
                        close = Double.valueOf(object[i][j].toString());
                    } else if (j == 3) {
                        high = Double.valueOf(object[i][j].toString());
                    } else if (j == 4) {
                        low = Double.valueOf(object[i][j].toString());
                    } else if (j == 5) {
                        volume = Double.valueOf(object[i][j].toString());
                    }
                    candleDto = new CandleDto(timeStamp, open, close, high, low, volume);
                }
                candleDtoList.add(candleDto);
            }
        }
        return candleDtoList;
    }

    public Candle mapToCandle(CandleDto candleDto, String currencyPair, String timeFrame) {
        Timestamp timestamp = new Timestamp(Long.valueOf(candleDto.getTimeStamp()));
        LocalDate localDate = timestamp.toLocalDateTime().toLocalDate();
        LocalTime localTime = timestamp.toLocalDateTime().toLocalTime();
        String date = localDate.toString();
        String time = localTime.toString();
        return  new Candle(null,
                currencyPair,
                candleDto.getTimeStamp(),
                date,
                time,
                candleDto.getOpen(),
                candleDto.getClose(),
                candleDto.getHigh(),
                candleDto.getLow(),
                candleDto.getVolume(),
                timeFrame);
    }

    public CandleDto mapToCandleDto(Candle candle) {
        return new CandleDto(candle.getTimeStamp(), candle.getOpen(), candle.getClose(), candle.getHigh(), candle.getLow(), candle.getVolume());
    }
}
