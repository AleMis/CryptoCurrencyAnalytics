package crypto_analytics.client.bitfinex;

import crypto_analytics.domain.bitfinex.dbupdater.DbUpdater;
import crypto_analytics.domain.bitfinex.ticker.TickerDto;
import crypto_analytics.request.bitfinex.RequestCreator;
import crypto_analytics.service.bitfinex.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TickerClient {

    @Autowired
    private RequestCreator requestCreator;

    @Autowired
    private DbService service;

    @Autowired
    private RestTemplate restTemplate;

//    @Bean
    private List<TickerDto> getTickerSet() throws InterruptedException {
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
            stringBuilder.append(requestCreator.getTickerGetRequest());
            stringBuilder.append(dbUpdater.getCurrencyPair().substring(1));
            requestsList.add(stringBuilder.toString());
            System.out.println(stringBuilder.toString());
        }
        return requestsList;
    }


}