package crypto_analytics.client.useraccount;

import com.google.gson.Gson;
import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeConnectionExceptions;
import crypto_analytics.authentication.ExchangeHttpResponse;
import crypto_analytics.domain.accountbalance.AccountBalanceListDto;
import crypto_analytics.domain.params.ParamsModerator;
import crypto_analytics.domain.params.ParamsToSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccountBalanceClient {

    @Value("${bitfinex.accountbalance.current}")
    private String accountBalance;

    @Autowired
    private ExchangeAuthentication exchangeAuthentication;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountBalanceClient.class);

    private static final String POST = "POST";

    public AccountBalanceListDto getAccountBalance(String params) throws Exception {

        ParamsToSearch paramsToSearch = null;
        ParamsModerator paramsModerator = new ParamsModerator(params, paramsToSearch);

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(accountBalance, POST, paramsModerator);

            LOGGER.info("Account balance information: " + exchangeHttpResponse);

            Gson gson = new Gson();

            return gson.fromJson(exchangeHttpResponse.getContent(), AccountBalanceListDto.class);
        } catch (Exception e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        }
    }
}
