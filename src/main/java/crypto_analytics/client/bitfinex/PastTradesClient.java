package crypto_analytics.client.bitfinex;

import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeConnectionExceptions;
import crypto_analytics.authentication.ExchangeHttpResponse;
import crypto_analytics.converter.bitfinex.PastTradesConverter;
import crypto_analytics.domain.bitfinex.params.ParamsModerator;
import crypto_analytics.domain.bitfinex.params.ParamsToSearch;
import crypto_analytics.domain.bitfinex.pasttrades.PastTradesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class PastTradesClient {

    @Value("${bitfinex.pasttrades}")
    private String pastTrades;

    @Autowired
    private ExchangeAuthentication exchangeAuthentication;

    @Autowired
    private PastTradesConverter pastTradesConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPermissionsClient.class);

    private static final String POST = "POST";

    private static final String PAST_TRADES = "past trades";

    @Bean
    public List<PastTradesDto> getPastTrades() throws Exception {

        //only for tests
        ParamsToSearch paramsToSearch = new ParamsToSearch("LTC",1496707230L);
        ParamsModerator paramsModerator = new ParamsModerator(PAST_TRADES, paramsToSearch);

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(pastTrades, POST, paramsModerator);

            LOGGER.info("Past trades information: " + exchangeHttpResponse);

            InputStream inputStream = new ByteArrayInputStream(exchangeHttpResponse.getContent());

            return pastTradesConverter.readPastTrades(inputStream);
        } catch (Exception e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        }
    }
}
