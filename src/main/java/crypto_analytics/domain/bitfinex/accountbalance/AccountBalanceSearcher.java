package crypto_analytics.domain.bitfinex.accountbalance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class AccountBalanceSearcher {

    private String currency;
    private Long sinceTimestamp;
}
