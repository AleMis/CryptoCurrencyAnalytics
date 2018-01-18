package crypto_analytics.authentication;

import crypto_analytics.domain.bitfinex.params.ParamsModerator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestParamsModifier {

    private static final String BALANCE_HISTORY = "balance history";
    private static final String PAST_TRADES = "past trades";


    public Map<String, Object> modifyRequestParamMap(ParamsModerator accountBalanceModerator) {
        HashMap<String, Object> createRequestParam = new HashMap<>();

        if (accountBalanceModerator.getParamType().equals(BALANCE_HISTORY)) {
            createRequestParam.put("currency", accountBalanceModerator.getAccountBalanceSearcher().getCurrency());
            createRequestParam.put("since", accountBalanceModerator.getAccountBalanceSearcher().getSinceTimestamp().toString());
        }else if(accountBalanceModerator.getParamType().equals(PAST_TRADES)) {
            createRequestParam.put("symbol", accountBalanceModerator.getAccountBalanceSearcher().getCurrency());
            createRequestParam.put("timestamp", accountBalanceModerator.getAccountBalanceSearcher().getSinceTimestamp().toString());
        }
        return createRequestParam;
    }
}
