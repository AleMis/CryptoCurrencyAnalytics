package crypto_analytics.controller;

import crypto_analytics.domain.candle.CandleDto;
import crypto_analytics.mapper.CandleMapper;
import crypto_analytics.request.RequestCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayDeque;

@RestController
public class CandleController {

    @Autowired
    private RequestCreator requestCreator;

    @Autowired
    private CandleMapper candleMapper;

    @Autowired
    private RestTemplateBuilder builder;

    @Bean
    private RestTemplate template() {
        return builder.build();
    }


    @Bean
    private Object[][] updateData(RestTemplate restTemplate) {

        ArrayDeque<String> queryQueue = requestCreator.returnQuery();

        Object[][] objectArray = null;
        for(String query : queryQueue) {
            System.out.println(query);
            ResponseEntity<Object[][]> responseEntity = restTemplate.getForEntity(query, Object[][].class);
            objectArray  = responseEntity.getBody();
        }

        CandleDto[][] candles = candleMapper.mapToCandletDtoArrays(objectArray);

        for(int i = 0; i<candles.length; i++) {
            for (int j = 0; j<candles[i].length; j++) {
                System.out.println(candles[i][j]);
            }
        }

        return objectArray;
    }


}
