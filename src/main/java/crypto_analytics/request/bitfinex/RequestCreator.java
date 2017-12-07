package crypto_analytics.request.bitfinex;

import crypto_analytics.domain.bitfinex.dbupdater.DbUpdater;
import crypto_analytics.service.bitfinex.DbService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Getter
public class RequestCreator {

    @Value("${bitfinex.ticker.request.get}")
    private String tickerGetRequest;

    @Value("${bitfinex.candle.request.get}")
    private String candleMainGetRequest;

    private static final String SECTION_HIST = "/hist";
    private static final String LIMIT = "?limit=160&";
    private static final String START = "start=";
    private static final String END = "&end=";
    private static final String SORT = "&sort=1";
    private static final String LAST = "/last";

    @Autowired
    private DbService service;

    public HashMap<String, List<String>> getHourlyRequestsListForDownload() {
        return getRequestsListForDownload(TimeFrame.TIME_FRAME_1H.getTimeFrame());
    }

    public HashMap<String, String> getHourlyRequestsListForUpdate() {
        return getRequestsListForUpdate(TimeFrame.TIME_FRAME_1H.getTimeFrame());
    }

    public HashMap<String, List<String>> getDailyRequestsListForDownload() {
        return getRequestsListForDownload(TimeFrame.TIME_FRAME_1D.getTimeFrame());
    }

    public HashMap<String, String> getDailyRequestsListForUpdate(){
        return getRequestsListForUpdate(TimeFrame.TIME_FRAME_1D.getTimeFrame());
    }

<<<<<<< HEAD:src/main/java/crypto_analytics/request/bitfinex/RequestCreator.java
    private  HashMap<String, List<String>> getRequestsListForDownload(String timeFrame) {
=======
    private  HashMap<String, List<String>> getRequestsListForDownload(String timeframe) {
>>>>>>> e39cef0... Code refactoring: changes in packages, class and methods names, simplifying few methods and class:src/main/java/crypto_analytics/request/bitfinex/RequestCreator.java
        HashMap<String, List<String>> requestMap = new HashMap<>();
        List<DbUpdater> dbUpdaterList = getUpdateList();
        List<String> requestList;
        for(DbUpdater dbUpdater : dbUpdaterList) {
            if(!dbUpdater.getIsDownload() && dbUpdater.getTimeFrame().equals(timeFrame)) {
                requestList = getRequestListForDownload(dbUpdater);
                LocalDateTime localDateTime = convertTimestampToLocalDateTime(dbUpdater.getEndTimestampForFirstDownload());
                dbUpdater.setUpdateDate(LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth()).toString());
                dbUpdater.setUpdateTime(localDateTime.toLocalTime().toString());
                dbUpdater.setUpdateTimestamp(dbUpdater.getEndTimestampForFirstDownload());
                dbUpdater.setIsDownload(true);
                service.saveDbUpdater(dbUpdater);
                requestMap.put(dbUpdater.getCurrencyPair(), requestList);
            }
        }
        return requestMap;
    }

<<<<<<< HEAD:src/main/java/crypto_analytics/request/bitfinex/RequestCreator.java
    private HashMap<String, String> getRequestsListForUpdate(String timeFrame) {
=======
    private HashMap<String, String> getRequestsListForUpdate(String timeframe) {
>>>>>>> e39cef0... Code refactoring: changes in packages, class and methods names, simplifying few methods and class:src/main/java/crypto_analytics/request/bitfinex/RequestCreator.java
        HashMap<String, String> requestMap = new HashMap<>();
        List<DbUpdater> dbUpdaterList = getUpdateList();
        String request;
        for(DbUpdater dbUpdater : dbUpdaterList)  {
            Long tmp = convertLocalDateTimeToLong(returnCurrentLocalDateTime(dbUpdater.getTimeFrame()));
            if(dbUpdater.getIsDownload() && dbUpdater.getTimeFrame().equals(timeFrame) && !dbUpdater.getUpdateTimestamp().equals(tmp)) {
                request = getRequestToUpdate(dbUpdater);
                LocalDateTime localDateTime = returnCurrentLocalDateTime(timeFrame);
                Long timestamp = convertLocalDateTimeToLong(localDateTime);
                dbUpdater.setUpdateDate(LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth()).toString());
                String time = localDateTime.getHour() + ":" + localDateTime.getMinute();
                dbUpdater.setUpdateTime(time);
                dbUpdater.setUpdateTimestamp(timestamp);
                service.saveDbUpdater(dbUpdater);
                requestMap.put(dbUpdater.getCurrencyPair(), request);
            }
        }
        return requestMap;
    }



    private String getRequestToUpdate(DbUpdater dbUpdater) {
        BigInteger startTimestamp = BigInteger.valueOf(dbUpdater.getUpdateTimestamp());
        BigInteger endTimestamp = BigInteger.valueOf(convertLocalDateTimeToLong(returnCurrentLocalDateTime(dbUpdater.getTimeFrame())));
        StringBuilder stringBuilder = new StringBuilder();
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
        return stringBuilder.toString();
    }

    private List<String> getRequestListForDownload(DbUpdater dbUpdater) {
        BigInteger startTimestamp = BigInteger.valueOf(dbUpdater.getStartTimestampForFirstDownload());
        BigInteger finalTimestamp = BigInteger.valueOf(dbUpdater.getEndTimestampForFirstDownload());
        BigInteger timestampDifference = new BigInteger(getTimeStampDifference(dbUpdater.getTimeFrame()));
        BigInteger midTimestamp = startTimestamp.add(timestampDifference.multiply(BigInteger.valueOf(150L)));
        List<String> requestList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        while(midTimestamp.compareTo(finalTimestamp)  == -1) {
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
            stringBuilder.append(midTimestamp);
            stringBuilder.append(SORT);
            requestList.add(stringBuilder.toString());
            startTimestamp = midTimestamp;
            midTimestamp = startTimestamp.add(timestampDifference.multiply(BigInteger.valueOf(150L)));
            System.out.println(stringBuilder.toString());
            if(midTimestamp.compareTo(finalTimestamp) == 1) {
                midTimestamp = finalTimestamp;
            }
        }
        String request = candleMainGetRequest + dbUpdater.getTimeFrame() + ":" + dbUpdater.getCurrencyPair() + SECTION_HIST + LIMIT + START + startTimestamp + END + midTimestamp + SORT;
        requestList.add(request);
        return requestList;
    }


    private List<DbUpdater> getUpdateList() {
        return service.getDbUpdaterList();
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

    private Long convertLocalDateTimeToLong(LocalDateTime localDateTime) {
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return timestamp.getTime();
    }

    private LocalDateTime convertTimestampToLocalDateTime(Long timestamp) {
        Timestamp ts = new Timestamp(timestamp);
        return LocalDateTime.ofInstant(ts.toInstant(), ZoneOffset.ofHours(0));
    }

    private LocalDateTime returnCurrentLocalDateTime(String timeFrame) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime time = null;
        switch (timeFrame) {
            case "1h":
                time = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonthValue(), currentDateTime.getDayOfMonth(), currentDateTime.getHour(), 0, 0, 0);
                break;
            case "1D":
                time = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonthValue(), currentDateTime.getDayOfMonth(), 0, 0, 0, 0);
                break;
        }
        return time;
    }
}
