package crypto_analytics.controller;

import crypto_analytics.domain.dbupdater.DbUpdater;
import crypto_analytics.domain.ticker.TickerDto;
import crypto_analytics.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<TickerDto> getTickerSet(RestTemplate restTemplate) throws InterruptedException {
        Set<TickerDto> tickerDtoSet = new HashSet<>();
        Set<String> querySet = getQueriesSet();

//        for(String query : querySet) {
//            TickerDto tickerDto = restTemplate.getForObject(query, TickerDto.class);
//            tickerDtoSet.add(tickerDto);
//            System.out.println(tickerDto);
//            Thread.sleep(6000);
//        }
        return tickerDtoSet;
    }

    private Set<String> getQueriesSet() {
        List<DbUpdater> list = service.getDbUpdaterList();
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> requestsSet = new HashSet<>();
        for(DbUpdater dbUpdater : list) {
            stringBuilder.delete(0, stringBuilder.length());
            stringBuilder.append(MAIN_GET_QUERY);
            stringBuilder.append(dbUpdater.getCurrencyPair());
            requestsSet.add(stringBuilder.toString());
        }
        return requestsSet;
    }


}
