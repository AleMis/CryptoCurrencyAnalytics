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
    private RequestCreator requestCreator;

    @Autowired
    private CandleMapper candleMapper;

    @Autowired
    private RestTemplateBuilder builder;

    @Autowired
    private DbService service;

    @Bean
    private RestTemplate template() {
        return builder.build();
    }



    @Bean
    private List<Candle> downloadBtcUsdCandles(RestTemplate restTemplate) throws InterruptedException {
        requestCreator.getRequestForBTCUSdownloading().stream().forEach(System.out::println);
        List<Object[][]> objects = modelController.downloadedData(restTemplate, requestCreator.getRequestForBTCUSdownloading());

        List<CandleDto> candlesDtoList = candleMapper.mapToCandletDtoArrays(objects);
        String currencyPair = requestCreator.getRequestForBTCUSdownloading().get(1).substring(45,52);
        String timeFrame = requestCreator.getRequestForBTCUSdownloading().get(1).substring(42,44);
        List<Candle> candlesList = new ArrayList<>();
        for(CandleDto candleDto : candlesDtoList) {
            Candle candleToSave = candleMapper.mapToCandle(candleDto, currencyPair, timeFrame);
            service.saveCandle(candleToSave);
            candlesList.add(candleMapper.mapToCandle(candleDto,currencyPair,timeFrame));
        }
        return candlesList;
    }
}
