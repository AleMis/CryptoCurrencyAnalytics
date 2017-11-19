package crypto_analytics.controller;

import crypto_analytics.domain.candle.Candle;
import crypto_analytics.domain.candle.CandleDto;
import crypto_analytics.mapper.CandleMapper;
import crypto_analytics.request.RequestCreator;
import crypto_analytics.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
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
    private RequestCreator requestCreator;

    @Bean
    private RestTemplate template() {
        return builder.build();
    }


    @Bean
    @Primary
    private List<Candle> downloadDailyCandles(RestTemplate restTemplate) throws InterruptedException {
        HashMap<String, List<String>> requestMap = requestCreator.getDailyRequestsListForDownload();
        HashMap<String, List<Object[][]>> objectsRequestMap = modelController.downloadedData(restTemplate,requestMap);
        List<CandleDto> candlesDtoList = candleMapper.mapToCandletDtoToDownload(objectsRequestMap);
        String timeFrame = "1D";
        return returnCandleList(candlesDtoList, timeFrame);
    }

    @Bean
    @Primary
    private List<Candle> downloadHourlyCandles(RestTemplate restTemplate) throws InterruptedException {
        HashMap<String, List<String>> requestMap = requestCreator.getHourlyRequestsListForDownload();
        HashMap<String, List<Object[][]>> objectsRequestMap = modelController.downloadedData(restTemplate,requestMap);
        List<CandleDto> candlesDtoList = candleMapper.mapToCandletDtoToDownload(objectsRequestMap);
        String timeFrame = "1h";
        return returnCandleList(candlesDtoList, timeFrame);
    }


    @Bean
    private List<Candle> updateDailyCandles(RestTemplate restTemplate) throws InterruptedException {
        HashMap<String, String> requestsList = requestCreator.getDailyRequestsListForUpdate();
        HashMap<String, Object[][]> requestMap = modelController.updateData(restTemplate,requestsList);
        List<CandleDto> candlesDtoList = candleMapper.mapToCandleDtoToUpdate(requestMap);
        String timeFrame = "1D";
        return returnCandleList(candlesDtoList, timeFrame);
    }

    @Bean
    private List<Candle> updateHourlyCandles(RestTemplate restTemplate) throws InterruptedException {
        HashMap<String, String> requestsList = requestCreator.getHourlyRequestsListForUpadte();
        HashMap<String, Object[][]> requestMap = modelController.updateData(restTemplate,requestsList);
        List<CandleDto> candlesDtoList = candleMapper.mapToCandleDtoToUpdate(requestMap);
        String timeFrame = "1h";
        return returnCandleList(candlesDtoList, timeFrame);
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
