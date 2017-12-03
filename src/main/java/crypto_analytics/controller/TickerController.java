package crypto_analytics.controller;

import crypto_analytics.domain.dbupdater.DbUpdater;
import crypto_analytics.domain.ticker.TickerDto;
import crypto_analytics.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TickerController {

    private static final String MAIN_GET_QUERY = "https://api.bitfinex.com/v1/pubticker/";

    @Autowired
    private DbService service;

    @Autowired
    private RestTemplateBuilder builder;

    @Bean
    private RestTemplate restTemplate() {
        return builder.build();
    }

    @Bean
    private List<TickerDto> getTickerSet(RestTemplate restTemplate) throws InterruptedException {
        List<TickerDto> tickerDtoList = new ArrayList<>();
        List<String> requestsList = getRequestsList();

        for(String query : requestsList) {
            TickerDto tickerDto = restTemplate.getForObject(query, TickerDto.class);
            tickerDtoList.add(tickerDto);
            System.out.println(tickerDto);
            Thread.sleep(6000);
        }
        return tickerDtoList;
    }

    private List<String> getRequestsList() {
        List<DbUpdater> list = service.getDbUpdaterList();
        StringBuilder stringBuilder = new StringBuilder();
        List<String> requestsList = new ArrayList<>();
        for(DbUpdater dbUpdater : list) {
            stringBuilder.delete(0, stringBuilder.length());
            stringBuilder.append(MAIN_GET_QUERY);
            stringBuilder.append(dbUpdater.getCurrencyPair());
            requestsList.add(stringBuilder.toString());
        }
        return requestsList;
    }


}
