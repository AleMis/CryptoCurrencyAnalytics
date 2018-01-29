package crypto_analytics.client.useraccount;

import com.google.gson.Gson;
import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeConnectionExceptions;
import crypto_analytics.authentication.ExchangeHttpResponse;
import crypto_analytics.domain.params.Params;
import crypto_analytics.domain.params.ParamsModerator;
import crypto_analytics.domain.params.ParamsToSearch;
import crypto_analytics.domain.pasttrades.PastTradesListDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PastTradesClient {

    @Value("${bitfinex.pasttrades}")
    private String pastTrades;

    @Autowired
    private ExchangeAuthentication exchangeAuthentication;

    private static final Logger LOGGER = LoggerFactory.getLogger(PastTradesClient.class);

    private static final String POST = "POST";

    public PastTradesListDto getPastTrades(String symbol, String timestamp) throws Exception {

        //only for tests
        ParamsToSearch paramsToSearch = new ParamsToSearch(symbol,Long.valueOf(timestamp));
        ParamsModerator paramsModerator = new ParamsModerator(Params.PAST_TRADES.getParams(), paramsToSearch);

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(pastTrades, POST, paramsModerator);

            LOGGER.info("Past trades information: " + exchangeHttpResponse);

            Gson gson = new Gson();
            PastTradesListDto pastTradesListDto =  gson.fromJson(exchangeHttpResponse.getContent(), PastTradesListDto.class);

            return pastTradesListDto;
        } catch (Exception e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        }
    }

}
