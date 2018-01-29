package crypto_analytics.client.marketdata;

import crypto_analytics.domain.DateManager;
import crypto_analytics.domain.candle.CandleKeyParameters;
import crypto_analytics.domain.candle.Candle;
import crypto_analytics.domain.candle.CandleDto;
import crypto_analytics.domain.dbupdater.DbUpdater;
import crypto_analytics.mapper.CandleMapper;
import crypto_analytics.service.DbService;
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
    private RestTemplate restTemplate;

    @Autowired
    private DateManager dateManager;

    @Autowired
    private CandleMapper candleMapper;

    @Autowired
    private DbService service;

    public void downloadAndSaveHistoricalData(HashMap<CandleKeyParameters, List<String>> requestMap) {
        LOGGER.info("Start of downloading historical data for candle chart.");
        for (Map.Entry<CandleKeyParameters, List<String>> requests : requestMap.entrySet()) {
            for (String httpRequest : requests.getValue()) {
                dowanloadData(requests.getKey(), httpRequest);
            }
        }
    }

    private void dowanloadData(CandleKeyParameters parameters, String httpRequest) {
        List<Object[][]> downloadedJsonDataList = new ArrayList<>();
        HashMap<CandleKeyParameters, List<Object[][]>> downloadedJsonDataWithKeyParameters = new HashMap<>();
        LOGGER.info("Downloading data (" + parameters.getCurrencyPair() + " : " + parameters.getTimeFrame() +") with request: " + httpRequest);
        try {
            ResponseEntity<String[][]> responseEntity = restTemplate.getForEntity(httpRequest, String[][].class);
            String[][] downloadedJsonData = responseEntity.getBody();
            downloadedJsonDataList.add(downloadedJsonData);
            downloadedJsonDataWithKeyParameters.put(parameters, downloadedJsonDataList);
            if(responseEntity.getBody().length != 0) {
                saveDataAndStatusAfterDownload(downloadedJsonDataWithKeyParameters, parameters);
            }
            Thread.sleep(6000);
        }catch(Exception e) {
            LOGGER.error(e.getMessage() + " Data: " + parameters.getCurrencyPair() + " : " + parameters.getTimeFrame() +" was not downloaded correctly." );
        }
    }

    private void saveDataAndStatusAfterDownload(HashMap<CandleKeyParameters, List<Object[][]>> downloadedJsonDataWithKeyParameters, CandleKeyParameters parameters) {
        saveStatusOfDownload(parameters.getCurrencyPair(), parameters.getTimeFrame());
        saveCandleList(candleMapper.mapToCandleDtoListForDownload(downloadedJsonDataWithKeyParameters), parameters.getTimeFrame());
        LOGGER.info("Historical data (" + parameters.getCurrencyPair() + " : " + parameters.getTimeFrame() + ") for candle chart has been downloaded and saved.");
    }

    public void updateAndSaveData(HashMap<CandleKeyParameters, String> requestMap) throws InterruptedException {
        LOGGER.info("Start of updating data for candle chart.");
        for (Map.Entry<CandleKeyParameters, String> requests : requestMap.entrySet()) {
            try {
                LOGGER.info("Updating data for: " + requests.getKey().getCurrencyPair(), requests.getKey().getTimeFrame() + ".");
                HashMap<CandleKeyParameters, Object[][]> downloadedJsonDataWithKeyParameters = new HashMap<>();
                ResponseEntity<String[][]> responseEntity = restTemplate.getForEntity(requests.getValue(), String[][].class);
                String[][] downloadedJsonData = responseEntity.getBody();
                downloadedJsonDataWithKeyParameters.put(requests.getKey(), downloadedJsonData);
                if (responseEntity.getBody().length != 0) {
                    saveDataAndStatusAfterUpdate(downloadedJsonDataWithKeyParameters, requests.getKey());
                }
                Thread.sleep(6000);
            }catch(Exception e){
                LOGGER.error("Data was not updated! " + e.getMessage());
            }
        }
    }

    private void saveDataAndStatusAfterUpdate(HashMap<CandleKeyParameters, Object[][]> downloadedJsonDataWithKeyParameters, CandleKeyParameters parameters) {
        saveCandleList(candleMapper.mapToCandleDtoListForUpdate(downloadedJsonDataWithKeyParameters), parameters.getTimeFrame());
        saveStatusOfUpdate(parameters.getCurrencyPair(), parameters.getTimeFrame());
        LOGGER.info("Data (" + parameters.getCurrencyPair() + " : " + parameters.getTimeFrame() + ") for candle chart has been updated!");
    }

    private void saveStatusOfDownload(String currencyPair, String timeFrame) {
        DbUpdater dbUpdater = findByCurrencyPairAndTimeFrame(currencyPair, timeFrame);
        LocalDateTime localDateTime = dateManager.convertTimestampToLocalDateTime(dbUpdater.getEndTimestampForFirstDownload());
        dbUpdater.setUpdateDate(LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth()).toString());
        dbUpdater.setUpdateTime(localDateTime.toLocalTime().toString());
        dbUpdater.setUpdateTimestamp(dbUpdater.getEndTimestampForFirstDownload());
        dbUpdater.setIsDownload(true);
        service.saveDbUpdater(dbUpdater);
    }

    private void saveStatusOfUpdate(String currencyPair, String timeFrame) {
        DbUpdater dbUpdater = findByCurrencyPairAndTimeFrame(currencyPair, timeFrame);
        LocalDateTime localDateTime = dateManager.returnCurrentLocalDateTime(timeFrame);
        Long timestamp = dateManager.convertLocalDateTimeToLong(localDateTime);
        dbUpdater.setUpdateDate(LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth()).toString());
        dbUpdater.setUpdateTime(localDateTime.getHour() + ":" + localDateTime.getMinute());
        dbUpdater.setUpdateTimestamp(timestamp);
        service.saveDbUpdater(dbUpdater);;
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
        return service.getDbUpdaterByCurrencyPairAndTimeFrame(currencyPair, timeFrame);
    }


}