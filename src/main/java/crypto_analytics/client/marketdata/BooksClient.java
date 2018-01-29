package crypto_analytics.client.marketdata;

import crypto_analytics.domain.books.Books;
import crypto_analytics.domain.books.BooksDto;
import crypto_analytics.domain.dbupdater.DbUpdater;
import crypto_analytics.mapper.BooksMapper;
import crypto_analytics.service.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class BooksClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BooksClient.class);

    private static final String levelOfPriceAggregation = "P0";
    private static final String numberOfPricePoints = "50";

    @Value("${bitfinex.books.request.get}")
    private String booksGetRequest;

    @Autowired
    private DbService service;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BooksMapper booksMapper;

//    @Scheduled(cron= "*/15 * * * * *")
    private void downloadBooksData() throws InterruptedException {
        LOGGER.info("Start of downloading order books data.");
        HashMap<String, String> httpRequestMap = createHttpRequest();
        for(Map.Entry<String, String> requests : httpRequestMap.entrySet()) {
            HashMap<String, Object[][]> downloadedBooksDataMap = new HashMap<>();
            try {
                ResponseEntity<String[][]> responseEntity = restTemplate.getForEntity(requests.getValue(), String[][].class);
                String[][] downloadedJsonData = responseEntity.getBody();
                if (responseEntity.getBody().length != 0) {
                    downloadedBooksDataMap.put(requests.getKey(), downloadedJsonData);
                    HashMap<String, List<BooksDto>> booksDtoMap = booksMapper.mapToBooksDtoList(downloadedBooksDataMap);
                    saveBooksData(booksMapper.mapBooksDtoListToBooksList(booksDtoMap), requests.getKey());
                }
                Thread.sleep(5000);
            }catch(Exception e) {
                LOGGER.error("Data was not downloaded! " + e.getMessage());
            }
        }
    }

    private void saveBooksData(List<Books> downloadedBooksList, String currencyPair) {
        deleteBooksByCurrencyPair(currencyPair);
        for (Books downloadedBooks : downloadedBooksList) {
            service.saveBooks(downloadedBooks);
            LOGGER.info("Order books data was saved: " + currencyPair);
        }
    }

    private void deleteBooksByCurrencyPair(String currencyPair) {
       try{
           LOGGER.info("Delete books order for " + currencyPair);
           service.deleteBooksByCurrencyPair(currencyPair);
       }catch(Exception e) {
           LOGGER.error("Deleting data for " + currencyPair + " not achieved!");
       }
    }

    private HashMap<String, String> createHttpRequest() {
        HashMap<String, String> httpRequestsMap = new HashMap<>();
        HashSet<String> currencyPairRequestSet = getCurrencyPairForHttpRequest();
        for(String currencyPair : currencyPairRequestSet) {
            String htttpRequest = buildHtttpRequest(currencyPair);
            httpRequestsMap.put(currencyPair, htttpRequest);
        }
        return httpRequestsMap;
    }

    private String buildHtttpRequest(String currencyPair) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(booksGetRequest);
        stringBuilder.append(currencyPair);
        stringBuilder.append("/");
        stringBuilder.append(levelOfPriceAggregation);
        stringBuilder.append("?len/");
        stringBuilder.append(numberOfPricePoints);
        return stringBuilder.toString();
    }

    private HashSet<String> getCurrencyPairForHttpRequest() {
        List<DbUpdater> dbUpdaterList = getDbUpdaterList();
        HashSet<String> currencyPairRequestSet = new HashSet<>();
        for(DbUpdater dbUpdater : dbUpdaterList) {
            currencyPairRequestSet.add(dbUpdater.getCurrencyPair());
        }
        return currencyPairRequestSet;
    }

    private List<DbUpdater> getDbUpdaterList() {
        return service.getDbUpdaterList();
    }

}


