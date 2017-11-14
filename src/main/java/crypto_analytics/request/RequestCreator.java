package crypto_analytics.request;

import crypto_analytics.domain.dbsearcher.DbSearcher;
import crypto_analytics.service.DbService;
import oracle.jrockit.jfr.StringConstantPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RequestCreator {

    //Main
    private static final String MAIN_REQUEST = "https://api.bitfinex.com/v2/candles/trade:";

    //TimeFrames
    private static final String TIME_FRAME_1M = "1m:";
    private static final String TIME_FRAME_5M = "5m:";
    private static final String TIME_FRAME_15M = "15m:";
    private static final String TIME_FRAME_30M = "30m:";
    private static final String TIME_FRAME_1H = "1h:";
    private static final String TIME_FRAME_3H = "3h:";
    private static final String TIME_FRAME_6H = "6h:";
    private static final String TIME_FRAME_12H = "12h:";
    private static final String TIME_FRAME_1D = "1D:";
    private static final String TIME_FRAME_7D = "7D:";
    private static final String TIME_FRAME_1MTH = "1M:";

    //Time Stamps
    private static final String DAILY_TIMESTAMP_DIFFERENCE = "86400000";

    //Currency Pairs
    private static final String BTC_USD = "tBTCUSD";
    private static final String BTCETH = "tBTCETH";

    //Additional
    private static final String SECTION_HIST = "/hist";
    private static final String LIMIT = "?limit=120&";
    private static final String START = "start=";
    private static final String END = "&end=";

    //final
    private static final String FINAL_DATE_1D = "1510358400000"; // 11.11.2017


    //BTCUSD
    private static final String START_DATE_BTCUSD_1H = "1504216800000";
    private static final String START_DATE_BTCUSD_1D = "1364853600000";
    private static final String START_DATE_BTCUSD_7D = "1364853600000";

    private static final String GET_BTCUSD_1D = MAIN_REQUEST + "" + TIME_FRAME_1D + "" + BTC_USD + "" + SECTION_HIST;


    @Autowired
    private DbService service;

    public List<String> getRequestForBTCUSdownloading() {
        List<String> requestList = new ArrayList<>();
        BigInteger finalDate = new BigInteger(FINAL_DATE_1D);
        BigInteger timeStampDifference = new BigInteger(DAILY_TIMESTAMP_DIFFERENCE);
        BigInteger startDate = new BigInteger(START_DATE_BTCUSD_1D);
        BigInteger endDate = startDate.add(timeStampDifference);

        while (endDate.compareTo(finalDate) == -1) {
            String request = GET_BTCUSD_1D + "" + LIMIT + "" + START + "" + startDate.toString() + "" + "" + END + "" + endDate.toString();
            requestList.add(request);
            startDate = endDate;
            endDate = startDate.add(timeStampDifference.multiply(BigInteger.valueOf(120)));
            if (endDate.compareTo(finalDate) == 1) {
                endDate = finalDate;
            }
        }
        return requestList;
    }


    public List<String> getHourlyRequestsList() {
        List<String> requestList = new ArrayList<>();
        List<DbSearcher> lastHourlyTimestampList = getLastDateFromDatabase(TIME_FRAME_1H);
        Long dailyTimestamp = convertLocalDatetTimeToLong(returnCurrentTimestamp(TIME_FRAME_1H));
        String request = null;
        for (DbSearcher searcher : lastHourlyTimestampList) {
            request = MAIN_REQUEST + "" + TIME_FRAME_1H + "" + searcher.getCurrencyPair() + SECTION_HIST + LIMIT + START + searcher.getTimestamp() + END + dailyTimestamp;
            requestList.add(request);
        }
        return requestList;
    }

    public List<String> getDailyRequestsList() {
        List<String> requestList = new ArrayList<>();
        List<DbSearcher> lastDailyTimestampList = getLastDateFromDatabase(TIME_FRAME_1D);
        Long dailyTimestamp = convertLocalDatetTimeToLong(returnCurrentTimestamp(TIME_FRAME_1D));
        String request = null;
        for (DbSearcher searcher : lastDailyTimestampList) {
            request = MAIN_REQUEST + "" + TIME_FRAME_1D + "" + searcher.getCurrencyPair() + SECTION_HIST + LIMIT + START + searcher.getTimestamp() + END + dailyTimestamp;
            requestList.add(request);
        }
        return requestList;
    }


    private List<DbSearcher> getLastDateFromDatabase(String timeframe) {
        List<DbSearcher> searcherList = new ArrayList<>();
        for (String currencyPair : getCurrencyList()) {
            Long lastTimestamp = service.getLastDateForCurrency(currencyPair, timeframe);
            searcherList.add(new DbSearcher(lastTimestamp, currencyPair, timeframe));
        }
        return searcherList;
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

    private List<String> getCurrencyList() {
        List<String> list = new ArrayList<>();
        list.add(BTC_USD);
        return list;
    }

    private Long convertLocalDatetTimeToLong(LocalDateTime localDateTime) {
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        Long timestampLong = timestamp.getTime();
        return timestampLong;
    }
}
