package crypto_analytics.mapper.bitfinex;

import crypto_analytics.domain.bitfinex.candle.Candle;
import crypto_analytics.domain.bitfinex.candle.CandleDto;
import crypto_analytics.domain.bitfinex.candle.CandleChartDto;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CandleMapper {

    public List<CandleDto> mapToCandleDtoListForDownload(HashMap<String, List<Object[][]>> twoDimObjectsMap) {
        List<CandleDto> candleDtoList = new ArrayList<>();
        for(Map.Entry<String, List<Object[][]>> map : twoDimObjectsMap.entrySet()) {
            candleDtoList = getCandleDtoListFromJsonList(map.getKey(), map.getValue());
        }
        return candleDtoList;
    }

    public List<CandleDto> mapToCandleDtoListForUpdate(HashMap<String, Object[][]> twoDimObjectsMap) {
        List<CandleDto> candleDtoList = new ArrayList<>();
        for(Map.Entry<String, Object[][]> map : twoDimObjectsMap.entrySet()) {
            candleDtoList = getCandleDtoListFromJson(map.getKey(), map.getValue());
        }
        return candleDtoList;
    }
    public List<CandleDto> getCandleDtoListFromJsonList(String currencyPair, List<Object[][]> objectsList) {
        List<CandleDto> candleDtoList = new ArrayList<>();
        for (Object[][] object : objectsList) {
            candleDtoList.addAll(getCandleDtoListFromJson(currencyPair, object));
        }
        return candleDtoList;
    }

    public List<CandleDto> getCandleDtoListFromJson(String currencyPair, Object[][] object) {
        List<CandleDto> candleDtoList = new ArrayList<>();
        for (int i = 0; i < object.length; i++) {
            Long timeStamp = Long.valueOf(object[i][0].toString());
            Double open = Double.valueOf(object[i][1].toString());
            Double close = Double.valueOf(object[i][2].toString());
            Double high = Double.valueOf(object[i][3].toString());
            Double low = Double.valueOf(object[i][4].toString());
            Double volume = Double.valueOf(object[i][5].toString());
            CandleDto candleDto = new CandleDto(timeStamp, currencyPair, open, close, high, low, volume);
            candleDtoList.add(candleDto);
        }
        return candleDtoList;
    }

    public Candle mapToCandle(CandleDto candleDto, String timeFrame) {
        Timestamp timestamp = new Timestamp(candleDto.getTimeStamp());
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

    public CandleChartDto[] mapToCandleDtoChartsList(List<Candle> list) {
        return list.stream().map(candle -> new CandleChartDto(
                candle.getTimeStamp(),
                candle.getOpen(),
                candle.getClose(),
                candle.getHigh(),
                candle.getLow(),
                candle.getVolume())).collect(Collectors.toList()).toArray(new CandleChartDto[list.size()]);
    }
}
