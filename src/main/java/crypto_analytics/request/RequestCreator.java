package crypto_analytics.request;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestCreator {

    private static final String GET_CANDLE_REQUEST = "https://api.bitfinex.com/v2/candles/trade:";

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

    private static final String SECTION_HIST = "/hist";

    private static final String BTCUSD = "tBTCUSD";
    private static final String BTCETH = "tBTCETH";

    private static final String END_DATE = LocalDate.now().toString();



    public ArrayDeque<String> returnQuery() {
        List<String> queryList = getQueryListWithTimeFramesAndCurrencyPairs();
        ArrayDeque<String> queryQueue = new ArrayDeque<>();
        for(String string : queryList) {
            queryQueue.offer(string + "" + SECTION_HIST);
        }

        return queryQueue;
    }

    private List<String> getQueryListWithTimeFramesAndCurrencyPairs() {
        List<String> queryListWithTimeFramesAndCurrencyPair  = new ArrayList<>();
        List<String> currencyPairList = getCurrencyPair();
        List<String> queryListWithTimeFrames = getQueryListWithTimeFrames();
        for(String currencyPair : currencyPairList) {
            for(String query : queryListWithTimeFrames) {
                queryListWithTimeFramesAndCurrencyPair.add(query + "" + currencyPair);
            }
        }
        return queryListWithTimeFramesAndCurrencyPair;
    }

    private List<String> getQueryListWithTimeFrames() {
        List<String> timeFrameList = getTimeFrameList();
        List<String> queryList = timeFrameList.stream().map(s -> GET_CANDLE_REQUEST + "" + s).collect(Collectors.toList());
        return queryList;
    }

    private List<String> getTimeFrameList() {
        List<String> timeFrameList = new ArrayList<>();
//        timeFrameList.add(TIME_FRAME_1M);
//        timeFrameList.add(TIME_FRAME_5M);
//        timeFrameList.add(TIME_FRAME_15M);
//        timeFrameList.add(TIME_FRAME_30M);
//        timeFrameList.add(TIME_FRAME_1H);
//        timeFrameList.add(TIME_FRAME_3H);
//        timeFrameList.add(TIME_FRAME_6H);
//        timeFrameList.add(TIME_FRAME_12H);
        timeFrameList.add(TIME_FRAME_1D);
//        timeFrameList.add(TIME_FRAME_7D);
//        timeFrameList.add(TIME_FRAME_1MTH);
        return  timeFrameList;
    }

    private List<String> getCurrencyPair() {
        List<String> currencyPairList = new ArrayList<>();
        currencyPairList.add(BTCUSD);
//        currencyPairList.add(BTCETH);
        return currencyPairList;
    }
}
