package crypto_analytics.client.bitfinex;

import crypto_analytics.domain.bitfinex.CandleKeyParameters;
import crypto_analytics.request.bitfinex.CandleHttpRequestCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class CandleClient {

    @Autowired
    private CandleDataClient candleDataClient;

    @Autowired
    private CandleHttpRequestCreator requestCreator;

    @EventListener(ApplicationReadyEvent.class)
    private void downloadHistoricalData() {
        downloadDailyCandles();
        downloadHourlyCandles();
    }

    private void downloadDailyCandles() {
        HashMap<CandleKeyParameters, List<String>> requestMap = requestCreator.getDailyRequestsListForDownload();
        candleDataClient.downloadAndSaveHistoricalData(requestMap);
        downloadHourlyCandles();
    }

    private void downloadHourlyCandles() {
        HashMap<CandleKeyParameters, List<String>> requestMap = requestCreator.getHourlyRequestsListForDownload();
        candleDataClient.downloadAndSaveHistoricalData(requestMap);
    }

    @Scheduled(cron="0 0 0 * * *")
    private void updateDailyCandles() throws InterruptedException {
        HashMap<CandleKeyParameters, String> requestsList = requestCreator.getDailyRequestsListForUpdate();
        candleDataClient.updateAndSaveData(requestsList);
    }

    @Scheduled(cron="0 0 * * * *")
    private void updateHourlyCandles() throws InterruptedException {
        HashMap<CandleKeyParameters, String> requestsList = requestCreator.getHourlyRequestsListForUpdate();
        candleDataClient.updateAndSaveData(requestsList);
    }
}