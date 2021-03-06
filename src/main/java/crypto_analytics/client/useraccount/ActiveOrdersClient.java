package crypto_analytics.client.useraccount;

import com.google.gson.Gson;
import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeConnectionExceptions;
import crypto_analytics.authentication.ExchangeHttpResponse;
import crypto_analytics.domain.order.CreatedOrderDto;
import crypto_analytics.domain.params.Params;
import crypto_analytics.domain.params.ParamsModerator;
import crypto_analytics.domain.params.ParamsToSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ActiveOrdersClient {

    @Value("${bitfinex.order.active}")
    private String activeOrders;

    @Autowired
    private ExchangeAuthentication exchangeAuthentication;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveOrdersClient.class);

    private static final String POST = "POST";

    public CreatedOrderDto[] getActiveOrders() throws Exception {

        ParamsToSearch paramsToSearch = null;
        ParamsModerator paramsModerator = new ParamsModerator(Params.WITHOUT_PARAMS.getParams(), paramsToSearch);

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(activeOrders, POST, paramsModerator);
            LOGGER.info("Active orders information: " + exchangeHttpResponse);

            Gson gson = new Gson();

            CreatedOrderDto[] createdOrderDtos = gson.fromJson(exchangeHttpResponse.getContent(), CreatedOrderDto[].class);

            return  createdOrderDtos;
        } catch (IOException e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        } catch (NullPointerException e) {
            LOGGER.error(ExchangeConnectionExceptions.CANCEL_ORDER_ERROR.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.CANCEL_ORDER_ERROR.getException(), e);
        }
    }
}
