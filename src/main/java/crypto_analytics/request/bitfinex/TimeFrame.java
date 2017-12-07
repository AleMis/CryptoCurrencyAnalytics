package crypto_analytics.request.bitfinex;

import lombok.Getter;

@Getter
public enum TimeFrame {

    TIME_FRAME_15M("15m"),
    TIME_FRAME_30M("30m"),
    TIME_FRAME_1H("1h"),
    TIME_FRAME_3H("3h"),
    TIME_FRAME_6H("6h"),
    TIME_FRAME_12H("12h"),
    TIME_FRAME_1D("1D"),
    TIME_FRAME_7D("7D"),
    TIME_FRAME_1MTH("1M");


    private final String timeFrame;

    TimeFrame(final String timeFrame) {
        this.timeFrame = timeFrame;
    }


}
