package crypto_analytics.controller;

import crypto_analytics.domain.candle.Candle;
import crypto_analytics.domain.candle.CandleDto;
import crypto_analytics.mapper.CandleMapper;
import crypto_analytics.request.RequestCreator;
import crypto_analytics.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@RestController
public class CandleController {

    @Autowired
    private ModelController modelController;

    @Autowired
    private CandleMapper candleMapper;

    @Autowired
    private RestTemplateBuilder builder;

    @Autowired
    private DbService service;

    @Autowired
    private RequestCreator requestCreator2;

    @Bean
    private RestTemplate template() {
        return builder.build();
    }

    @Bean
    private List<Candle> downalodDailyCandles(RestTemplate restTemplate) throws InterruptedException {
        List<Object[][]> objects = modelController.downloadedData(restTemplate, requestCreator2.getDailyRequestsList());
        List<CandleDto> candlesDtoList = candleMapper.mapToCandletDtoArrays(objects);
        String currencyPair = requestCreator2.getDailyRequestsList().getUpdateList().get(0).substring(45,52);
        String timeFrame = "1D";
        return returnCandleList(candlesDtoList, currencyPair, timeFrame);
    }


    private List<Candle> returnCandleList(List<CandleDto> candlesDtoList, String currencyPair, String timeFrame) {
        List<Candle> candleList = new ArrayList<>();
        for(CandleDto candleDto : candlesDtoList) {
            Candle candleToSave = candleMapper.mapToCandle(candleDto, currencyPair, timeFrame);
            service.saveCandle(candleToSave);
            candleList.add(candleMapper.mapToCandle(candleDto, currencyPair, timeFrame));
        }
        return candleList;
    }
}
