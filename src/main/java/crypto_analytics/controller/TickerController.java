package crypto_analytics.controller;

import crypto_analytics.domain.symbol.Symbol;
import crypto_analytics.domain.ticker.TickerDto;
import crypto_analytics.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
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
        Set<Symbol> symbolSet = service.getSymbols();
        Set<String> queriesSet = symbolSet.stream().map(symbol -> MAIN_GET_QUERY + "" + symbol.getSymbol()).collect(Collectors.toSet());
        return queriesSet;
    }


}
