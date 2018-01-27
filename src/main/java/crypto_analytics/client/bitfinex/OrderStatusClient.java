package crypto_analytics.client.bitfinex;

import com.google.gson.Gson;
import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeConnectionExceptions;
import crypto_analytics.authentication.ExchangeHttpResponse;
import crypto_analytics.domain.bitfinex.order.CreatedOrderDto;
import crypto_analytics.domain.bitfinex.params.Params;
import crypto_analytics.domain.bitfinex.params.ParamsModerator;
import crypto_analytics.domain.bitfinex.params.ParamsToSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrderStatusClient {

    @Value("${bitfinex.order.status}")
    private String orderStatus;

    @Autowired
    private ExchangeAuthentication exchangeAuthentication;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatusClient.class);

    private static final String POST = "POST";

    public CreatedOrderDto getOrdersStatus(String orderId) throws Exception {

        ParamsToSearch paramsToSearch = new ParamsToSearch(orderId);
        ParamsModerator paramsModerator = new ParamsModerator(Params.ORDER_BY_ID.getParams(), paramsToSearch);

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(orderStatus, POST, paramsModerator);
            LOGGER.info("Order status: " + exchangeHttpResponse);

            Gson gson = new Gson();

            return gson.fromJson(exchangeHttpResponse.getContent(), CreatedOrderDto.class);
        } catch (IOException e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        } catch (NullPointerException e) {
            LOGGER.error(ExchangeConnectionExceptions.ORDER_STATUS_ERROR.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.ORDER_STATUS_ERROR.getException(), e);
        }
    }
}
