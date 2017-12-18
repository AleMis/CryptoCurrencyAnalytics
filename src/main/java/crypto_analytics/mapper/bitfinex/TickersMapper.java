package crypto_analytics.mapper.bitfinex;

import crypto_analytics.domain.bitfinex.tickers.Tickers;
import crypto_analytics.domain.bitfinex.tickers.TickersDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TickersMapper {

    public Tickers mapTickersDtoToTickers(TickersDto tickersDto) {
        return new Tickers(
                0,
                tickersDto.getCurrencyPair(),
                tickersDto.getBid(),
                tickersDto.getBidSize(),
                tickersDto.getAsk(),
                tickersDto.getAskSize(),
                tickersDto.getDailyChange(),
                tickersDto.getDailyChangePerc(),
                tickersDto.getLastPrice(),
                tickersDto.getVolume(),
                tickersDto.getHigh(),
                tickersDto.getLow());
    }

    public List<Tickers> mapTickersArrayToListTickers(String[][] tickersData) {
        List<Tickers> tickersList = new ArrayList<>();
        for(int i = 0; i<tickersData.length; i++) {
            String currencyPair = tickersData[i][0];
            Double bid = Double.valueOf(tickersData[i][1]);
            Double bidSize = Double.valueOf(tickersData[i][2]);
            Double ask = Double.valueOf(tickersData[i][3]);
            Double askSize = Double.valueOf(tickersData[i][4]);
            Double dailyChange = Double.valueOf(tickersData[i][5]);
            Double dailyChangePerc = Double.valueOf(tickersData[i][6]);
            Double lastPrice = Double.valueOf(tickersData[i][7]);
            Double volume = Double.valueOf(tickersData[i][8]);
            Double high = Double.valueOf(tickersData[i][9]);
            Double low = Double.valueOf(tickersData[i][10]);
            TickersDto tickersDto = new TickersDto(currencyPair, bid, bidSize, ask, askSize, dailyChange, dailyChangePerc, lastPrice, volume, high, low);
            Tickers tickers = mapTickersDtoToTickers(tickersDto);
            tickersList.add(tickers);
        }
        return tickersList;
    }

    public List<TickersDto> mapTickersListToTickersDtoList(List<Tickers> tickersList) {
        return tickersList.stream()
                .map(tickers -> new TickersDto(
                        tickers.getCurrencyPair(),
                        tickers.getBid(),
                        tickers.getBidSize(),
                        tickers.getAsk(),
                        tickers.getAskSize(),
                        tickers.getDailyChange(),
                        tickers.getDailyChangePerc(),
                        tickers.getLow(),
                        tickers.getVolume(),
                        tickers.getHigh(),
                        tickers.getLow())).collect(Collectors.toList());
    }
}
