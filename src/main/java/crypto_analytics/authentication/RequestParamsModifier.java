package crypto_analytics.authentication;

import crypto_analytics.domain.bitfinex.params.ParamsModerator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestParamsModifier {

    private static final String BALANCE_HISTORY = "BALANCE_HISTORY";
    private static final String BALANCE_HISTORY_SINCE = "BALANCE_HISTORY_SINCE";
    private static final String BALANCE_HISTORY_SINCE_WALLET ="BALANCE_HISTORY_SINCE_WALLET";
    private static final String BALANCE_HISTORY_SINCE_UNTIL_WALLET ="BALANCE_HISTORY_SINCE_UNTIL_WALLET";
    private static final String PAST_TRADES = "PAST_TRADES";


    public Map<String, Object> modifyRequestParamMap(ParamsModerator accountBalanceModerator) {
        HashMap<String, Object> createRequestParam = new HashMap<>();

        if (accountBalanceModerator.getParamType().equals(BALANCE_HISTORY)) {
            createRequestParam = createReqeustParamForBalanceHistory(accountBalanceModerator);
        }else if(accountBalanceModerator.getParamType().equals(PAST_TRADES)) {
            createRequestParam.put("symbol", accountBalanceModerator.getAccountBalanceSearcher().getCurrency());
            createRequestParam.put("timestamp", accountBalanceModerator.getAccountBalanceSearcher().getSinceTimestamp().toString());
        }
        return createRequestParam;
    }

    private HashMap<String, Object> createReqeustParamForBalanceHistory(ParamsModerator paramsModerator) {
        HashMap<String, Object> createRequestParam = new HashMap<>();
        createRequestParam.put("currency", paramsModerator.getAccountBalanceSearcher().getCurrency());
        createRequestParam.put("since", paramsModerator.getAccountBalanceSearcher().getSinceTimestamp().toString());
        switch(paramsModerator.getParamType()) {
            case BALANCE_HISTORY_SINCE:
                return createRequestParam;
            case BALANCE_HISTORY_SINCE_WALLET:
                createRequestParam.put("wallet", paramsModerator.getAccountBalanceSearcher().getWallet());
                return createRequestParam;
            case BALANCE_HISTORY_SINCE_UNTIL_WALLET:
                createRequestParam.put("until", paramsModerator.getAccountBalanceSearcher().getUntilTimestamp().toString());
                createRequestParam.put("wallet", paramsModerator.getAccountBalanceSearcher().getWallet());
                return createRequestParam;
        }
        return createRequestParam;
    }
}
