package crypto_analytics.client.bitfinex;

import crypto_analytics.domain.bitfinex.DateManager;
import crypto_analytics.domain.bitfinex.KeyParameters;
import crypto_analytics.domain.bitfinex.candle.Candle;
import crypto_analytics.domain.bitfinex.candle.CandleDto;
import crypto_analytics.domain.bitfinex.dbupdater.DbUpdater;
import crypto_analytics.mapper.bitfinex.CandleMapper;
import crypto_analytics.repository.DbUpdaterRepository;
import crypto_analytics.request.bitfinex.TimeFrame;
import crypto_analytics.service.bitfinex.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CandleDataClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CandleDataClient.class);

    @Autowired
    private DbUpdaterRepository dbUpdaterRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DateManager dateManager;

    @Autowired
    private CandleMapper candleMapper;

    @Autowired
    private DbService service;

    public void downloadData(HashMap<KeyParameters, List<String>> requestMap) {
        LOGGER.info("Start of downloading historical data for candle chart.");

        String[][] requestTwoDArray;
        List<Object[][]> requestTwoDList = new ArrayList<>();
        HashMap<KeyParameters, List<Object[][]>> requestTwoDArrayMap = new HashMap<>();
        for (Map.Entry<KeyParameters, List<String>> requests : requestMap.entrySet()) {
            for (String request : requests.getValue()) {
                System.out.println(request);
                System.out.println(requests.getKey());
                try {
                    ResponseEntity<String[][]> responseEntity = restTemplate.getForEntity(request, String[][].class);
                    requestTwoDArray = responseEntity.getBody();
                    requestTwoDList.add(requestTwoDArray);
                    requestTwoDArrayMap.put(requests.getKey(), requestTwoDList);

                    if(responseEntity.getBody().length != 0) {
                        saveStatusOfDownload(requests.getKey().getCurrencyPair(), requests.getKey().getTimeFrame());
                        saveCandleList(candleMapper.mapToCandleDtoListForDownload(requestTwoDArrayMap), requests.getKey().getTimeFrame());
                        requestTwoDList.clear();
                        requestTwoDArrayMap.clear();
                        LOGGER.info("Historical data (" + requests.getKey().getCurrencyPair() + " : " + requests.getKey().getTimeFrame() + ") for candle chart has been downloaded and saved.");
                        Thread.sleep(6000);
                    }
                }catch(Exception e) {
                    LOGGER.error(e.getMessage() + " Data: " + requests.getKey().getCurrencyPair() + " : " + requests.getKey().getTimeFrame() +" was not downloaded." );
                }
            }
        }
    }

    public void updateData(HashMap<KeyParameters, String> requestMap) throws InterruptedException {
        LOGGER.info("Start of updating data for candle chart.");

        String[][] requestTwoDArray;
        HashMap<KeyParameters, Object[][]> actualDataMap = new HashMap<>();

        for (Map.Entry<KeyParameters, String> requests : requestMap.entrySet()) {
            try {
                System.out.println(requests.getKey().getCurrencyPair());
                ResponseEntity<String[][]> responseEntity = restTemplate.getForEntity(requests.getValue(), String[][].class);
                if (responseEntity.getBody().length != 0) {
                    requestTwoDArray = responseEntity.getBody();
                    actualDataMap.put(requests.getKey(), requestTwoDArray);
                    saveCandleList(candleMapper.mapToCandleDtoListForUpdate(actualDataMap), requests.getKey().getTimeFrame());
                    saveStatusOfUpdate(requests.getKey().getCurrencyPair(), requests.getKey().getTimeFrame());
                    LOGGER.info("Data (" + requests.getKey().getCurrencyPair() + " : " + requests.getKey().getTimeFrame() + ") for candle chart has been updated!");
                    Thread.sleep(6000);
                }
            }catch(Exception e){
                LOGGER.error("Data was not updated! " + e.getMessage());
            }
        }
    }

    private void saveStatusOfDownload(String currencyPair, String timeFrame) {
        DbUpdater dbUpdater = findByCurrencyPairAndTimeFrame(currencyPair, timeFrame);
        LocalDateTime localDateTime = dateManager.convertTimestampToLocalDateTime(dbUpdater.getEndTimestampForFirstDownload());
        dbUpdater.setUpdateDate(LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth()).toString());
        dbUpdater.setUpdateTime(localDateTime.toLocalTime().toString());
        dbUpdater.setUpdateTimestamp(dbUpdater.getEndTimestampForFirstDownload());
        dbUpdater.setIsDownload(true);
        dbUpdaterRepository.save(dbUpdater);
    }

    private void saveStatusOfUpdate(String currencyPair, String timeFrame) {
        DbUpdater dbUpdater = findByCurrencyPairAndTimeFrame(currencyPair, timeFrame);
        LocalDateTime localDateTime = dateManager.returnCurrentLocalDateTime(timeFrame);
        Long timestamp = dateManager.convertLocalDateTimeToLong(localDateTime);
        dbUpdater.setUpdateDate(LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth()).toString());
        dbUpdater.setUpdateTime(localDateTime.getHour() + ":" + localDateTime.getMinute());
        dbUpdater.setUpdateTimestamp(timestamp);
        dbUpdaterRepository.save(dbUpdater);
    }

    private List<Candle> saveCandleList(List<CandleDto> candlesDtoList, String timeFrame) {
        List<Candle> candleList = new ArrayList<>();
        for(CandleDto candleDto : candlesDtoList) {
            Candle candleToSave = candleMapper.mapToCandle(candleDto, timeFrame);
            service.saveCandle(candleToSave);
            candleList.add(candleMapper.mapToCandle(candleDto, timeFrame));
        }
        return candleList;
    }

    private DbUpdater findByCurrencyPairAndTimeFrame(String currencyPair, String timeFrame) {
        return dbUpdaterRepository.findByCurrencyPairAndTimeFrame(currencyPair, timeFrame);
    }


}