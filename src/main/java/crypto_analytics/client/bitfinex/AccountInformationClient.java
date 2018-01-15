package crypto_analytics.client.bitfinex;

import com.google.gson.Gson;
import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeConnectionExceptions;
import crypto_analytics.authentication.ExchangeHttpResponse;
import crypto_analytics.converter.bitfinex.AccountBalanceConverter;
import crypto_analytics.converter.bitfinex.PermisionsConverter;
import crypto_analytics.domain.bitfinex.accountbalance.AccountBalanceDto;
import crypto_analytics.domain.bitfinex.permisions.PermisionsDto;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccountInformationClient {

    @Value("${bitfinex.accountinformation.history}")
    private String history;

    @Value("${bitfinex.permisions.info}")
    private String permisions;

    @Autowired
    private ExchangeAuthentication exchangeAuthentication;

    @Autowired
    private PermisionsConverter permisionsConverter;

    @Autowired
    private AccountBalanceConverter accountBalanceConverter;;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountInformationClient.class);

    private static final String POST = "POST";

    private Gson gson = new Gson();


    //Remember!!! When you don not have any cryptocurrencies on your account
    // this method will return empty array. Server returns only information about
    // cryptocurrencies which you have on your account!

    @Bean
    public List<AccountBalanceDto> getBalanceHistory() throws Exception {

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(history, POST);

            LOGGER.info("Account balance information: " + exchangeHttpResponse);

            InputStream inputStream = new ByteArrayInputStream(exchangeHttpResponse.getContent());

            return accountBalanceConverter.readAccountsBalances(inputStream);
        } catch (Exception e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        }
    }

    public ArrayList<PermisionsDto> getPermisionsList() throws Exception {

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(permisions, POST);

            LOGGER.info("Information about permisions to different resources: " + exchangeHttpResponse);

            InputStream inputStream = new ByteArrayInputStream(exchangeHttpResponse.getContent());

            return permisionsConverter.readPermissionsList(inputStream);
        }catch (IOException e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        }
    }
}
