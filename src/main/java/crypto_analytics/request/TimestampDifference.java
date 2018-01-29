package crypto_analytics.request;

import lombok.Getter;

@Getter
public enum TimestampDifference {

    //Time Stamps
    DAILY_TIMESTAMP_DIFFERENCE("86400000"),
    HOURLY_TIMESTAMP_DIFFERENCE("3600000");

    private final String timestampDifference;

    TimestampDifference(String timestampDifference) {
        this.timestampDifference = timestampDifference;
    }


}