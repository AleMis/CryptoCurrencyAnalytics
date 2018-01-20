package crypto_analytics.domain.bitfinex.params;

import lombok.Getter;

@Getter
public enum Params {


    BALANCE_HISTORY("BALANCE_HISTORY"),
    BALANCE_HISTORY_SINCE("BALANCE_HISTORY_SINCE"),
    BALANCE_HISTORY_SINCE_WALLET("BALANCE_HISTORY_SINCE_WALLET"),
    BALANCE_HISTORY_SINCE_UNTIL_WALLET("BALANCE_HISTORY_SINCE_UNTIL_WALLET"),
    PAST_TRADES("PAST_TRADES"),
    WITHOUT_PARAMS("WITHOUT_PARAMS");

    private String params;

    Params(final String params) {
        this.params = params;
    }
}