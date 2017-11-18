package crypto_analytics.request;

import crypto_analytics.domain.dbupdater.DbUpdaterContainer;
import crypto_analytics.domain.dbupdater.DbUpdater;
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

    private List<String> finalDownloadRequestList= new ArrayList<>();
    private List<String> finalUpdateRequestList = new ArrayList<>();

    private DbUpdaterContainer dbUpdaterContainer = new DbUpdaterContainer();

    public DbUpdaterContainer getHourlyRequestList() {
        return getRequestsList(TIME_FRAME_1H);
    }

    public DbUpdaterContainer getDailyRequestsList() {
        return getRequestsList(TIME_FRAME_1D);
    }

    public DbUpdaterContainer getRequestsList(String timeframe) {
        finalDownloadRequestList.clear();
        finalUpdateRequestList.clear();

        List<DbUpdater> dbUpdaterList = getUpdateList();
        List<String> requestList = new ArrayList<>();
        String requestForUpdate = null;

        for(DbUpdater dbUpdater : dbUpdaterList) {
            if(!dbUpdater.getIsDownload() && dbUpdater.getTimeFrame().equals(timeframe)) {
                requestList = getRequestListForDownload(dbUpdater);
                requestList.stream().forEach(System.out::println);
                LocalDateTime localDateTime = returnCurrentTimestamp(timeframe);
                Long timestamp = convertLocalDatetTimeToLong(localDateTime);
                dbUpdater.setIsDownload(true);
                dbUpdater.setUpdateDate(LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth()).toString());
                String time = localDateTime.getHour() + ":" + localDateTime.getMinute();
                dbUpdater.setUpdateTime(time);
                dbUpdater.setUpdateTimestamp(timestamp);
                service.saveDbUpdater(dbUpdater);
            }
        }
        finalDownloadRequestList.addAll(requestList);
        dbUpdaterList = getUpdateList();

        for(DbUpdater dbUpdater : dbUpdaterList)  {
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
        dbUpdaterContainer.setDownloadList(finalDownloadRequestList);
        dbUpdaterContainer.setUpdateList(finalUpdateRequestList);
        return dbUpdaterContainer;
    }

    public String getRequestToUpdate(DbUpdater update) {
        BigInteger startTimestamp = BigInteger.valueOf(update.getUpdateTimestamp());
        BigInteger endTimestamp = BigInteger.valueOf(convertLocalDatetTimeToLong(returnCurrentTimestamp(update.getTimeFrame())));
        return MAIN_REQUEST + update.getTimeFrame() + ":" + update.getCurrencyPair() + SECTION_HIST + LIMIT + START + startTimestamp + END + endTimestamp + SORT;
    }

    public List<String> getRequestListForDownload(DbUpdater dbUpdater) {
        BigInteger startTimestamp = BigInteger.valueOf(dbUpdater.getStartTimestampForFirstDownload());
        BigInteger finalTimestamp = BigInteger.valueOf(dbUpdater.getEndTimestampForFirstDownload());
        BigInteger timestampDifference = new BigInteger(getTimeStampDifference(dbUpdater.getTimeFrame()));
        BigInteger midTimestamp = startTimestamp.add(timestampDifference);
        List<String> requestList = new ArrayList<>();
        while(midTimestamp.compareTo(finalTimestamp) == -1) {
            String request = MAIN_REQUEST + dbUpdater.getTimeFrame() + ":" + dbUpdater.getCurrencyPair() + SECTION_HIST + LIMIT + START + startTimestamp + END + midTimestamp + SORT;
            System.out.println(request);
            requestList.add(request);
            startTimestamp = midTimestamp;
            midTimestamp = startTimestamp.add(timestampDifference.multiply(BigInteger.valueOf(120)));

            if(midTimestamp.compareTo(finalTimestamp) == 1) {
                midTimestamp = finalTimestamp;
            }
        }
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
                //need to add hourly timestamp difference
                break;
        }
        return result;
    }

    private Long convertLocalDatetTimeToLong(LocalDateTime localDateTime) {
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return timestamp.getTime();
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
