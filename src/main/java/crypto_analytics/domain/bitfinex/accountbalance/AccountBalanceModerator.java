package crypto_analytics.domain.bitfinex.accountbalance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class AccountBalanceModerator {

    private String paramType;
    private AccountBalanceSearcher accountBalanceSearcher;
}
