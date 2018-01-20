package crypto_analytics.client.bitfinex;

import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeConnectionExceptions;
import crypto_analytics.authentication.ExchangeHttpResponse;
import crypto_analytics.converter.bitfinex.AccountBalanceHistoryConverter;
import crypto_analytics.domain.bitfinex.accountbalance.AccountBalanceHistoryDto;
import crypto_analytics.domain.bitfinex.params.ParamsModerator;
import crypto_analytics.domain.bitfinex.params.ParamsToSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class AccountBalanceHistoryClient {

    @Value("${bitfinex.accountbalance.history}")
    private String accountHistory;

    @Autowired
    private ExchangeAuthentication exchangeAuthentication;

    @Autowired
    private AccountBalanceHistoryConverter accountBalanceHistoryConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPermissionsClient.class);

    private static final String POST = "POST";

    public List<AccountBalanceHistoryDto> getAccountBalanceHistory(ParamsToSearch paramsToSearch, String param) throws Exception {

        ParamsModerator paramsModerator = new ParamsModerator(param, paramsToSearch);

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(accountHistory, POST, paramsModerator);

            LOGGER.info("Account balance history information: " + exchangeHttpResponse);

            InputStream inputStream = new ByteArrayInputStream(exchangeHttpResponse.getContent());

            return accountBalanceHistoryConverter.readAccountsBalanceHistory(inputStream);
        } catch (Exception e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        }
    }
}
