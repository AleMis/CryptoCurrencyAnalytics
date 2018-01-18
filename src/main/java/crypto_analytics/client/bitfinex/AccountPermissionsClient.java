package crypto_analytics.client.bitfinex;

import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeConnectionExceptions;
import crypto_analytics.authentication.ExchangeHttpResponse;
import crypto_analytics.converter.bitfinex.PermisionsConverter;
import crypto_analytics.domain.bitfinex.accountbalance.AccountBalanceModerator;
import crypto_analytics.domain.bitfinex.accountbalance.AccountBalanceSearcher;
import crypto_analytics.domain.bitfinex.permisions.PermisionsDto;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;

@Component
public class AccountPermissionsClient {

    @Value("${bitfinex.permisions.info}")
    private String permisions;

    @Autowired
    private ExchangeAuthentication exchangeAuthentication;

    @Autowired
    private PermisionsConverter permisionsConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPermissionsClient.class);

    private static final String POST = "POST";

    private static final String WITHOUT_PARAM = "without param";

    public ArrayList<PermisionsDto> getPermisionsList() throws Exception {

        AccountBalanceSearcher accountBalanceSearcher = null;
        AccountBalanceModerator accountBalanceModerator = new AccountBalanceModerator(WITHOUT_PARAM, accountBalanceSearcher);

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(permisions, POST, accountBalanceModerator);

            LOGGER.info("Information about permisions to different resources: " + exchangeHttpResponse);

            InputStream inputStream = new ByteArrayInputStream(exchangeHttpResponse.getContent());

            return permisionsConverter.readPermissionsList(inputStream);
        }catch (IOException e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        }
    }
}
