package crypto_analytics.request;

import crypto_analytics.domain.dbupdater.DbUpdater;
import crypto_analytics.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RequestCreator {
    //Main
    private static final String MAIN_REQUEST = "https://api.bitfinex.com/v2/candles/trade:";

    //TimeFrames
    private static final String TIME_FRAME_15M = "15m";
    private static final String TIME_FRAME_30M = "30m";
    private static final String TIME_FRAME_1H = "1h";
    private static final String TIME_FRAME_3H = "3h";
    private static final String TIME_FRAME_6H = "6h";
    private static final String TIME_FRAME_12H = "12h";
    private static final String TIME_FRAME_1D = "1D";
    private static final String TIME_FRAME_7D = "7D";
    private static final String TIME_FRAME_1MTH = "1M";


    //Time Stamps
    private static final String DAILY_TIMESTAMP_DIFFERENCE = "86400000";
    private static final String HOURLY_TIMESTAMP_DIFFERENCE = "3600000";

    //Additional
    private static final String SECTION_HIST = "/hist";
    private static final String LIMIT = "?limit=160&";
    private static final String START = "start=";
    private static final String END = "&end=";
    private static final String SORT = "&sort=1";
    private static final String LAST = "/last";

    @Autowired
    private DbService service;

    public HashMap<String, List<String>> getHourlyRequestsListForDownload() {
        return getRequestsListForDownload(TIME_FRAME_1H);
    }

    public HashMap<String, String> getHourlyRequestsListForUpadte() {
        return getRequestsListForUpdate(TIME_FRAME_1H);
    }

    public HashMap<String, List<String>> getDailyRequestsListForDownload() {
        return getRequestsListForDownload(TIME_FRAME_1D);
    }

    public HashMap<String, String> getDailyRequestsListForUpdate(){
        return getRequestsListForUpdate(TIME_FRAME_1D);
    }

    public  HashMap<String, List<String>> getRequestsListForDownload(String timeframe) {
        HashMap<String, List<String>> requestMap = new HashMap<>();
        List<DbUpdater> dbUpdaterList = getUpdateList();
        List<String> requestList = new ArrayList<>();
        for(DbUpdater dbUpdater : dbUpdaterList) {
            if(!dbUpdater.getIsDownload() && dbUpdater.getTimeFrame().equals(timeframe)) {
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

    public HashMap<String, String> getRequestsListForUpdate(String timeframe) {
        HashMap<String, String> requestMap = new HashMap<>();
        List<DbUpdater> dbUpdaterList = getUpdateList();
        String request = null;
        for(DbUpdater dbUpdater : dbUpdaterList)  {
            Long tmp = convertLocalDateTimeToLong(returnCurrentLocalDateTime(dbUpdater.getTimeFrame()));
            if(dbUpdater.getIsDownload() && dbUpdater.getTimeFrame().equals(timeframe) && !dbUpdater.getUpdateTimestamp().equals(tmp)) {
                request = getRequestToUpdate(dbUpdater);
                LocalDateTime localDateTime = returnCurrentLocalDateTime(timeframe);
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



    public String getRequestToUpdate(DbUpdater update) {
        BigInteger startTimestamp = BigInteger.valueOf(update.getUpdateTimestamp());
        BigInteger endTimestamp = BigInteger.valueOf(convertLocalDateTimeToLong(returnCurrentLocalDateTime(update.getTimeFrame())));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MAIN_REQUEST);
        stringBuilder.append(update.getTimeFrame());
        stringBuilder.append(":");
        stringBuilder.append(update.getCurrencyPair());
        stringBuilder.append(SECTION_HIST);
        stringBuilder.append(LIMIT);
        stringBuilder.append(START);
        stringBuilder.append(startTimestamp);
        stringBuilder.append(END);
        stringBuilder.append(endTimestamp);
        stringBuilder.append(SORT);
        return stringBuilder.toString();
    }

    public List<String> getRequestListForDownload(DbUpdater dbUpdater) {
        BigInteger startTimestamp = BigInteger.valueOf(dbUpdater.getStartTimestampForFirstDownload());
        BigInteger finalTimestamp = BigInteger.valueOf(dbUpdater.getEndTimestampForFirstDownload());
        BigInteger timestampDifference = new BigInteger(getTimeStampDifference(dbUpdater.getTimeFrame()));
        BigInteger midTimestamp = startTimestamp.add(timestampDifference.multiply(BigInteger.valueOf(150L)));
        List<String> requestList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        while(midTimestamp.compareTo(finalTimestamp)  == -1) {
            stringBuilder.delete(0, stringBuilder.length());
            stringBuilder.append(MAIN_REQUEST);
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
        String request = MAIN_REQUEST + dbUpdater.getTimeFrame() + ":" + dbUpdater.getCurrencyPair() + SECTION_HIST + LIMIT + START + startTimestamp + END + midTimestamp + SORT;
        requestList.add(request);
        return requestList;
    }

    private List<DbUpdater> getUpdateList() {
        return service.getDbUpdaterList();
    }
 

    public String getTimeStampDifference(String timeFrame) {
        String result = null;
        switch(timeFrame) {
            case TIME_FRAME_1D:
                result = DAILY_TIMESTAMP_DIFFERENCE;
                break;
            case TIME_FRAME_1H:
                result = HOURLY_TIMESTAMP_DIFFERENCE;
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
            case TIME_FRAME_1H:
                time = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonthValue(), currentDateTime.getDayOfMonth(), currentDateTime.getHour(), 0, 0, 0);
                break;
            case TIME_FRAME_1D:
                time = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonthValue(), currentDateTime.getDayOfMonth(), 0, 0, 0, 0);
                break;
        }
        return time;
    }
}
