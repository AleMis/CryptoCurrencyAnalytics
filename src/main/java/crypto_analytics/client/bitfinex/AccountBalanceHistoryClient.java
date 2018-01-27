package crypto_analytics.client.bitfinex;

import com.google.gson.Gson;
import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeConnectionExceptions;
import crypto_analytics.authentication.ExchangeHttpResponse;
import crypto_analytics.domain.bitfinex.accountbalance.AccountBalanceHistoryListDto;
import crypto_analytics.domain.bitfinex.params.ParamsModerator;
import crypto_analytics.domain.bitfinex.params.ParamsToSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccountBalanceHistoryClient {

    @Value("${bitfinex.accountbalance.history}")
    private String accountHistory;

    @Autowired
    private ExchangeAuthentication exchangeAuthentication;


    private static final Logger LOGGER = LoggerFactory.getLogger(AccountBalanceHistoryClient.class);

    private static final String POST = "POST";

    public AccountBalanceHistoryListDto getAccountBalanceHistory(ParamsToSearch paramsToSearch, String param) throws Exception {

        ParamsModerator paramsModerator = new ParamsModerator(param, paramsToSearch);

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(accountHistory, POST, paramsModerator);

            LOGGER.info("Account balance history information: " + exchangeHttpResponse);

            Gson gson = new Gson();

            AccountBalanceHistoryListDto accountBalanceHistoryDtos = gson.fromJson(exchangeHttpResponse.getContent(), AccountBalanceHistoryListDto.class);
            accountBalanceHistoryDtos.stream().forEach(System.out::println);

            return accountBalanceHistoryDtos;
        } catch (Exception e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        }
    }
}
