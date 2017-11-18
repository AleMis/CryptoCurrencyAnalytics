package crypto_analytics.mapper;

import crypto_analytics.domain.candle.Candle;
import crypto_analytics.domain.candle.CandleDto;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CandleMapper {

    public List<CandleDto> mapToCandletDtoToDownload(HashMap<String, List<Object[][]>> twoDimObjectsMap) {
        List<CandleDto> candleDtoList = new ArrayList<>();
        for(Map.Entry<String, List<Object[][]>> map : twoDimObjectsMap.entrySet()) {
            for (Object[][] object : map.getValue()) {
                Long timeStamp = null;
                String currencyPair = map.getKey();
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
                        candleDto = new CandleDto(timeStamp, currencyPair, open, close, high, low, volume);
                    }
                    candleDtoList.add(candleDto);
                }
            }
        }
        return candleDtoList;
    }

    public List<CandleDto> mapToCandleDtoToUpdate(HashMap<String, Object[][]> twoDimObjectsMap) {
        List<CandleDto> candleDtoList = new ArrayList<>();
        for(Map.Entry<String, Object[][]> map : twoDimObjectsMap.entrySet()) {
            Long timeStamp = null;
            String currencyPair = map.getKey();
            Double open = null;
            Double close = null;
            Double high = null;
            Double low = null;
            Double volume = null;

            for (int i = 0; i < map.getValue().length; i++) {
                CandleDto candleDto = null;
                for (int j = 0; j < map.getValue()[i].length; j++) {
                    if (j == 0) {
                        timeStamp = Long.valueOf(map.getValue()[i][j].toString());
                    } else if (j == 1) {
                        open = Double.valueOf(map.getValue()[i][j].toString());
                    } else if (j == 2) {
                        close = Double.valueOf(map.getValue()[i][j].toString());
                    } else if (j == 3) {
                        high = Double.valueOf(map.getValue()[i][j].toString());
                    } else if (j == 4) {
                        low = Double.valueOf(map.getValue()[i][j].toString());
                    } else if (j == 5) {
                        volume = Double.valueOf(map.getValue()[i][j].toString());
                    }
                    candleDto = new CandleDto(timeStamp, currencyPair, open, close, high, low, volume);
                }
                candleDtoList.add(candleDto);
            }
        }
        return candleDtoList;
    }

    public Candle mapToCandle(CandleDto candleDto, String timeFrame) {
        Timestamp timestamp = new Timestamp(Long.valueOf(candleDto.getTimeStamp()));
        LocalDate localDate = timestamp.toLocalDateTime().toLocalDate();
        LocalTime localTime = timestamp.toLocalDateTime().toLocalTime();
        String date = localDate.toString();
        String time = localTime.toString();
        return  new Candle(null,
                candleDto.getCurrencyPair(),
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
        return new CandleDto(candle.getTimeStamp(),candle.getCurrencyPair() ,candle.getOpen(), candle.getClose(), candle.getHigh(), candle.getLow(), candle.getVolume());
    }
}
