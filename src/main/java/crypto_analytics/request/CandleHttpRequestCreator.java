package crypto_analytics.request;

import crypto_analytics.domain.DateManager;
import crypto_analytics.domain.candle.CandleKeyParameters;
import crypto_analytics.domain.dbupdater.DbUpdater;
import crypto_analytics.service.DbService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Getter
public class CandleHttpRequestCreator {

    @Value("${bitfinex.ticker.request.get}")
    private String tickerGetRequest;

    @Value("${bitfinex.tickers.request.get}")
    private String tickersGetRequest;

    @Value("${bitfinex.candle.request.get}")
    private String candleMainGetRequest;

    private static final String SECTION_HIST = "/hist";
    private static final String LIMIT = "?limit=120&";
    private static final String START = "start=";
    private static final String END = "&end=";
    private static final String SORT = "&sort=1";
    private static final String LAST = "/last";

    @Autowired
    private DateManager dateManager;

    @Autowired
    private DbService service;

    public HashMap<CandleKeyParameters, List<String>> getHourlyRequestsListForDownload() {
        return getRequestsListForDownload(TimeFrame.TIME_FRAME_1H.getTimeFrame());
    }

    public HashMap<CandleKeyParameters, String> getHourlyRequestsListForUpdate() {
        return getRequestsListForUpdate(TimeFrame.TIME_FRAME_1H.getTimeFrame());
    }

    public HashMap<CandleKeyParameters, List<String>> getDailyRequestsListForDownload() {
        return getRequestsListForDownload(TimeFrame.TIME_FRAME_1D.getTimeFrame());
    }

    public HashMap<CandleKeyParameters, String> getDailyRequestsListForUpdate(){
        return getRequestsListForUpdate(TimeFrame.TIME_FRAME_1D.getTimeFrame());
    }

    private  HashMap<CandleKeyParameters, List<String>> getRequestsListForDownload(String timeFrame) {
        HashMap<CandleKeyParameters, List<String>> requestMap = new HashMap<>();
        List<DbUpdater> dbUpdaterList = getUpdateList();
        List<String> requestList;
        for(DbUpdater dbUpdater : dbUpdaterList) {
            if(!dbUpdater.getIsDownload() && dbUpdater.getTimeFrame().equals(timeFrame)) {
                requestList = getRequestListToDownload(dbUpdater);
                CandleKeyParameters keyParameters = new CandleKeyParameters(dbUpdater.getCurrencyPair(), dbUpdater.getTimeFrame());
                requestMap.put(keyParameters, requestList);
            }
        }
        return requestMap;
    }

    private HashMap<CandleKeyParameters, String> getRequestsListForUpdate(String timeFrame) {
        HashMap<CandleKeyParameters, String> requestMap = new HashMap<>();
        List<DbUpdater> dbUpdaterList = getUpdateList();
        String request;
        for(DbUpdater dbUpdater : dbUpdaterList)  {
            Long tmp = dateManager.convertLocalDateTimeToLong(dateManager.returnCurrentLocalDateTime(dbUpdater.getTimeFrame()));
            if(dbUpdater.getIsDownload() && dbUpdater.getTimeFrame().equals(timeFrame) && !dbUpdater.getUpdateTimestamp().equals(tmp)) {
                request = getRequestToUpdate(dbUpdater);
                CandleKeyParameters keyParameters = new CandleKeyParameters(dbUpdater.getCurrencyPair(), dbUpdater.getTimeFrame());
                requestMap.put(keyParameters, request);
            }
        }
        return requestMap;
    }

    private String getRequestToUpdate(DbUpdater dbUpdater) {
        BigInteger startTimestamp = BigInteger.valueOf(dbUpdater.getUpdateTimestamp());
        BigInteger endTimestamp = BigInteger.valueOf(dateManager.convertLocalDateTimeToLong(dateManager.returnCurrentLocalDateTime(dbUpdater.getTimeFrame())));
        return createHttpRequest(dbUpdater, startTimestamp, endTimestamp).toString();
    }

    private List<String> getRequestListToDownload(DbUpdater dbUpdater) {
        BigInteger startTimestamp = BigInteger.valueOf(dbUpdater.getStartTimestampForFirstDownload());
        BigInteger finalTimestamp = BigInteger.valueOf(dbUpdater.getEndTimestampForFirstDownload());
        BigInteger timestampDifference = new BigInteger(getTimeStampDifference(dbUpdater.getTimeFrame()));
        BigInteger midTimestamp = startTimestamp.add(timestampDifference.multiply(BigInteger.valueOf(120L)));
        List<String> requestList = new ArrayList<>();
        while(midTimestamp.compareTo(finalTimestamp)  <= -1) {
            StringBuilder stringBuilder = createHttpRequest(dbUpdater, startTimestamp, midTimestamp);
            requestList.add(stringBuilder.toString());
            startTimestamp = midTimestamp;
            midTimestamp = startTimestamp.add(timestampDifference.multiply(BigInteger.valueOf(120L)));
            if(midTimestamp.compareTo(finalTimestamp) >= 1) {
                midTimestamp = finalTimestamp;
            }
        }
        String request = candleMainGetRequest + dbUpdater.getTimeFrame() + ":" + dbUpdater.getCurrencyPair() + SECTION_HIST + LIMIT + START + startTimestamp + END + midTimestamp + SORT;
        requestList.add(request);
        return requestList;
    }

    private StringBuilder createHttpRequest(DbUpdater dbUpdater, BigInteger startTimestamp, BigInteger endTimestamp) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(candleMainGetRequest);
        stringBuilder.append(dbUpdater.getTimeFrame());
        stringBuilder.append(":");
        stringBuilder.append(dbUpdater.getCurrencyPair());
        stringBuilder.append(SECTION_HIST);
        stringBuilder.append(LIMIT);
        stringBuilder.append(START);
        stringBuilder.append(startTimestamp);
        stringBuilder.append(END);
        stringBuilder.append(endTimestamp);
        stringBuilder.append(SORT);
        return stringBuilder;
    }

    private String getTimeStampDifference(String timeFrame) {
        String result = null;
        switch(timeFrame) {
            case "1D":
                result = TimestampDifference.DAILY_TIMESTAMP_DIFFERENCE.getTimestampDifference();
                break;
            case "1h":
                result = TimestampDifference.HOURLY_TIMESTAMP_DIFFERENCE.getTimestampDifference();
                break;
        }
        return result;
    }

    private List<DbUpdater> getUpdateList() {
        return service.getDbUpdaterList();
    }

}