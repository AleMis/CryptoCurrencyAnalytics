package crypto_analytics.client.bitfinex;

import crypto_analytics.domain.bitfinex.KeyParameters;
import crypto_analytics.request.bitfinex.RequestCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.HashMap;
import java.util.List;

@Configuration
public class CandleClient {

    @Autowired
    private CandleDataClient candleDataClient;

    @Autowired
    private RequestCreator requestCreator;

    @EventListener(ApplicationReadyEvent.class)
    private void downloadDailyCandles() throws InterruptedException {
        HashMap<KeyParameters, List<String>> requestMap = requestCreator.getDailyRequestsListForDownload();
        candleDataClient.downloadData(requestMap);
    }

    @EventListener(ApplicationReadyEvent.class)
    private void downloadHourlyCandles() throws InterruptedException {
        HashMap<KeyParameters, List<String>> requestMap = requestCreator.getHourlyRequestsListForDownload();
        candleDataClient.downloadData(requestMap);
    }

    @Scheduled(cron="0 0 0 * * *")
    private void updateDailyCandles() throws InterruptedException {
        HashMap<KeyParameters, String> requestsList = requestCreator.getDailyRequestsListForUpdate();
        candleDataClient.updateData(requestsList);
    }

    @Scheduled(cron="0 0 * * * *")
    private void updateHourlyCandles() throws InterruptedException {
        HashMap<KeyParameters, String> requestsList = requestCreator.getHourlyRequestsListForUpdate();
        candleDataClient.updateData(requestsList);
    }
}