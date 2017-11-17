package crypto_analytics.request;

import crypto_analytics.domain.dbsearcher.DbUpdaterList;
import crypto_analytics.domain.dbsearcher.DbUpdater;
import crypto_analytics.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    //Additional
    private static final String SECTION_HIST = "/hist";
    private static final String LIMIT = "?limit=120&";
    private static final String START = "start=";
    private static final String END = "&end=";
    private static final String SORT = "&sort=1";
    private static final String LAST = "/last";

    @Autowired
    private DbService service;


    public DbUpdaterList getHourlyRequestList() {
        return getRequestsList(TIME_FRAME_1H);
    }

    public DbUpdaterList getDailyRequestsList() {
        return getRequestsList(TIME_FRAME_1D);
    }

    public DbUpdaterList getRequestsList(String timeframe) {
        List<DbUpdater> updateList = getUpdateList();
        List<String> finalDownloadRequestList = new ArrayList<>();
        List<String> finalUpdateRequestList = new ArrayList<>();
        List<String> requestList = new ArrayList<>();
        String requestForUpdate = null;

        for(DbUpdater dbUpdater : updateList) {
            if(!dbUpdater.getIsDownload() && dbUpdater.getTimeFrame().equals(timeframe)) {
                requestList = getRequestListForDownload(dbUpdater);
                LocalDateTime localDateTime = returnCurrentTimestamp(timeframe);
                Long timestamp = convertLocalDatetTimeToLong(localDateTime);
                dbUpdater.setIsDownload(true);
                dbUpdater.setUpdateDate(LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth()).toString());
                String time = localDateTime.getHour() + ":" + localDateTime.getMinute();
                dbUpdater.setUpdateTime(time);
                dbUpdater.setUpdateTimestamp(timestamp);
                service.saveDbUpdater(dbUpdater);
                //w tym miejscu lub w getRequestListForDownload trzeba zrobić jeszcze zapisanie w bazie danych ze pobrano dane dla danego timeframu
            }
            finalDownloadRequestList.addAll(requestList);
        }

        for(DbUpdater dbUpdater : updateList)  {
            if(dbUpdater.getIsDownload() && dbUpdater.getTimeFrame().equals(timeframe)) {
                requestForUpdate = getRequestToUpdate(dbUpdater);
                finalUpdateRequestList.add(requestForUpdate);
                LocalDateTime localDateTime = returnCurrentTimestamp(timeframe);
                Long timestamp = convertLocalDatetTimeToLong(localDateTime);
                dbUpdater.setUpdateDate(LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth()).toString());
                String time = localDateTime.getHour() + ":" + localDateTime.getMinute();
                dbUpdater.setUpdateTime(time);
                dbUpdater.setUpdateTimestamp(timestamp);
                service.saveDbUpdater(dbUpdater);
            }
        }
        return new DbUpdaterList(finalDownloadRequestList, finalUpdateRequestList);
    }

    public String getRequestToUpdate(DbUpdater update) {
        BigInteger startTimestamp = BigInteger.valueOf(update.getUpdateTimestamp());
        BigInteger endTimestamp = BigInteger.valueOf(convertLocalDatetTimeToLong(returnCurrentTimestamp(update.getTimeFrame())));
        String request = MAIN_REQUEST + update.getTimeFrame() + ":" + update.getCurrencyPair() + SECTION_HIST + LIMIT + START + startTimestamp + END + endTimestamp + SORT;
        return request;
    }

    public List<String> getRequestListForDownload(DbUpdater update) {
        BigInteger startTimestamp = BigInteger.valueOf(update.getStartTimestampForFirstDownload());
        BigInteger finalTimestamp = BigInteger.valueOf(update.getEndTimestampForFirstDownload());
        BigInteger timestampDifference = new BigInteger(getTimeStampDifference(update.getTimeFrame()));
        BigInteger midTimestamp = startTimestamp.add(timestampDifference);
        List<String> requestList = new ArrayList<>();
        while(midTimestamp.compareTo(finalTimestamp) == -1) {
            String request = MAIN_REQUEST + update.getTimeFrame() + ":" + update.getCurrencyPair() + SECTION_HIST + LIMIT + START + startTimestamp + END + midTimestamp + SORT;
            requestList.add(request);
        }
        return requestList;
    }

    public String getTimeStampDifference(String timeFrame) {
        String result = null;
        switch(timeFrame) {
            case TIME_FRAME_1D:
                result = DAILY_TIMESTAMP_DIFFERENCE;
                break;
            case TIME_FRAME_1H:

                break;
        }
        return result;
    }

    private List<DbUpdater> getUpdateList() {
        return service.getDbUpdaterList();
    }

    private Long convertLocalDatetTimeToLong(LocalDateTime localDateTime) {
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        Long timestampLong = timestamp.getTime();
        return timestampLong;
    }

    private LocalDateTime returnCurrentTimestamp(String timeframe) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime time = null;
        switch (timeframe) {
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
