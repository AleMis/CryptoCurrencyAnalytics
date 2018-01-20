package crypto_analytics.controller.bitfinex;

import crypto_analytics.client.bitfinex.AccountBalanceHistoryClient;
import crypto_analytics.domain.bitfinex.accountbalance.AccountBalanceHistoryDto;
import crypto_analytics.domain.bitfinex.params.Params;
import crypto_analytics.domain.bitfinex.params.ParamsToSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/crypto")
public class AccountBalanceHistoryController {

    @Autowired
    private AccountBalanceHistoryClient accountBalanceHistoryClient;


    @RequestMapping(method = RequestMethod.GET, value = "/balance-history")
    private List<AccountBalanceHistoryDto> getUserAccountBalanceHistory(@RequestParam String currency,
                                                                        @RequestParam Long since,
                                                                        @RequestParam(required = false) Long until,
                                                                        @RequestParam(required = false) String wallet) throws Exception {

        ParamsToSearch paramsToSearch = new ParamsToSearch(currency, since, until, wallet);
        return accountBalanceHistoryClient.getAccountBalanceHistory(paramsToSearch, Params.BALANCE_HISTORY.getParams());
    }
}
