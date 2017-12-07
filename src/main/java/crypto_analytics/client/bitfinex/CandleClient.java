package crypto_analytics.client.bitfinex;

import crypto_analytics.domain.bitfinex.candle.Candle;
import crypto_analytics.domain.bitfinex.candle.CandleDto;
import crypto_analytics.mapper.bitfinex.CandleMapper;
import crypto_analytics.request.bitfinex.RequestCreator;
import crypto_analytics.request.bitfinex.TimeFrame;
import crypto_analytics.service.bitfinex.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
public class CandleClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CandleClient.class);

    @Autowired
    private CandleDataClient modelController;

    @Autowired
    private CandleMapper candleMapper;

    @Autowired
    private DbService service;

    @Autowired
    private RequestCreator requestCreator;


    @Bean
    @Primary
    private List<Candle> downloadDailyCandles() throws InterruptedException {
        HashMap<String, List<String>> requestMap = requestCreator.getDailyRequestsListForDownload();
        HashMap<String, List<Object[][]>> objectsRequestMap = modelController.downloadData(requestMap);
        List<CandleDto> candlesDtoList = candleMapper.mapToCandleDtoListForDownload(objectsRequestMap);
        return returnCandleList(candlesDtoList, TimeFrame.TIME_FRAME_1D.getTimeFrame());
    }

    @Bean
    @Primary
    private List<Candle> downloadHourlyCandles() throws InterruptedException {
        HashMap<String, List<String>> requestMap = requestCreator.getHourlyRequestsListForDownload();
        HashMap<String, List<Object[][]>> objectsRequestMap = modelController.downloadData(requestMap);
        List<CandleDto> candlesDtoList = candleMapper.mapToCandleDtoListForDownload(objectsRequestMap);
        return returnCandleList(candlesDtoList, TimeFrame.TIME_FRAME_1H.getTimeFrame());
    }


    @Bean
    private List<Candle> updateDailyCandles() throws InterruptedException {
        HashMap<String, String> requestsList = requestCreator.getDailyRequestsListForUpdate();
        HashMap<String, Object[][]> requestMap = modelController.updateData(requestsList);
        List<CandleDto> candlesDtoList = candleMapper.mapToCandleDtoListForUpdate(requestMap);
        return returnCandleList(candlesDtoList, TimeFrame.TIME_FRAME_1D.getTimeFrame());
    }

    @Bean
    private List<Candle> updateHourlyCandles() throws InterruptedException {
        HashMap<String, String> requestsList = requestCreator.getHourlyRequestsListForUpdate();
        HashMap<String, Object[][]> requestMap = modelController.updateData(requestsList);
        List<CandleDto> candlesDtoList = candleMapper.mapToCandleDtoListForUpdate(requestMap);
        return returnCandleList(candlesDtoList, TimeFrame.TIME_FRAME_1H.getTimeFrame());
    }


    private List<Candle> returnCandleList(List<CandleDto> candlesDtoList, String timeFrame) {
        List<Candle> candleList = new ArrayList<>();
        for(CandleDto candleDto : candlesDtoList) {
            Candle candleToSave = candleMapper.mapToCandle(candleDto, timeFrame);
            service.saveCandle(candleToSave);
            candleList.add(candleMapper.mapToCandle(candleDto, timeFrame));
        }
        return candleList;
    }
}
