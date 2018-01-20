package crypto_analytics.controller.bitfinex;

import crypto_analytics.client.bitfinex.AccountBalanceClient;
import crypto_analytics.domain.bitfinex.accountbalance.AccountBalanceDto;
import crypto_analytics.domain.bitfinex.params.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/crypto")
public class AccountBalanceController {

    @Autowired
    private AccountBalanceClient accountBalanceClient;

    @RequestMapping(method = RequestMethod.GET, value = "/balance")
    private List<AccountBalanceDto> getUserAccountBalance() throws Exception {
        return accountBalanceClient.getAccountBalance(Params.WITHOUT_PARAMS.getParams());
    }
}
