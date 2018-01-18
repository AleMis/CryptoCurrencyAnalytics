package crypto_analytics.authentication;

import crypto_analytics.domain.bitfinex.accountbalance.AccountBalanceModerator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestParamsModifier {

    private static final String BALANCE_HISTORY = "balance history";

    public Map<String, Object> modifyRequestParamMap(AccountBalanceModerator accountBalanceModerator) {
        HashMap<String, Object> createRequestParam = new HashMap<>();

        if (accountBalanceModerator.getParamType().equals(BALANCE_HISTORY)) {
                createRequestParam.put("currency", accountBalanceModerator.getAccountBalanceSearcher().getCurrency());
                createRequestParam.put("since", accountBalanceModerator.getAccountBalanceSearcher().getSinceTimestamp().toString());
        }
        return createRequestParam;
    }
}
