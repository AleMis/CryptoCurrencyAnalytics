package crypto_analytics.client.marketdata;

import crypto_analytics.domain.dbupdater.DbUpdater;
import crypto_analytics.domain.tickers.Tickers;
import crypto_analytics.mapper.TickersMapper;
import crypto_analytics.request.CandleHttpRequestCreator;
import crypto_analytics.service.DbService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@AllArgsConstructor
@Component
public class TickersClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BooksClient.class);

    @Autowired
    private CandleHttpRequestCreator requestCreator;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DbService service;

    @Autowired
    private TickersMapper tickersMapper;

//    @Scheduled(cron= "*/30 * * * * *")
    private void downloadTickersData() throws InterruptedException {
        String request = getRequest();
        LOGGER.info("Start of downloading tickers data [request: " + request + "]");
        ResponseEntity<String[][]> responseEntity = restTemplate.getForEntity(request, String[][].class);
        String[][] tickersData = responseEntity.getBody();
        if(tickersData != null) {
            List<Tickers> tickersList = tickersMapper.mapTickersArrayToListTickers(tickersData);
            deleteTickersData();
            for(Tickers tickers : tickersList) {
                saveTickersData(tickers);
            }
        }
    }

    private void saveTickersData(Tickers tickers) {
        service.saveTickers(tickers);
        LOGGER.info("New data for tickers (" + tickers.getCurrencyPair() + ") was saved!");
    }

    private void deleteTickersData() {
        try {
            LOGGER.info("Old tickers data was deleted!");
            service.deleteAllTickers();
        }catch(Exception e) {
            LOGGER.error(e + ". Deleting all tickers data was not achieved!");
        }
    }

    private String getRequest() {
        List<DbUpdater> list = service.getDbUpdaterList();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(requestCreator.getTickersGetRequest());
        for(DbUpdater dbUpdater : list) {
            stringBuilder.append(dbUpdater.getCurrencyPair());
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }
}
