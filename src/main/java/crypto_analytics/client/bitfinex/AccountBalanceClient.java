package crypto_analytics.client.bitfinex;

import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeConnectionExceptions;
import crypto_analytics.authentication.ExchangeHttpResponse;
import crypto_analytics.converter.bitfinex.AccountBalanceConverter;
import crypto_analytics.domain.bitfinex.accountbalance.AccountBalanceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class AccountBalanceClient {

    @Value("${bitfinex.accountbalance.current}")
    private String accountBalance;

    @Autowired
    private ExchangeAuthentication exchangeAuthentication;

    @Autowired
    private AccountBalanceConverter accountBalanceConverter;;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPermissionsClient.class);

    private static final String POST = "POST";


    public List<AccountBalanceDto> getAccountBalance() throws Exception {

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(accountBalance, POST);

            LOGGER.info("Account balance information: " + exchangeHttpResponse);

            InputStream inputStream = new ByteArrayInputStream(exchangeHttpResponse.getContent());

            return accountBalanceConverter.readAccountsBalances(inputStream);
        } catch (Exception e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        }
    }
}
