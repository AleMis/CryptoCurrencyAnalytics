package crypto_analytics.client.useraccount;

import com.google.gson.Gson;
import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeConnectionExceptions;
import crypto_analytics.authentication.ExchangeHttpResponse;
import crypto_analytics.domain.order.CreatedOrderDto;
import crypto_analytics.domain.order.OrderDto;
import crypto_analytics.domain.params.ParamsModerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NewOrderClient {

    @Value("${bitfinex.order.new}")
    private String newOrder;

    @Autowired
    private ExchangeAuthentication exchangeAuthentication;

    private static final Logger LOGGER = LoggerFactory.getLogger(NewOrderClient.class);

    private static final String POST = "POST";

    public CreatedOrderDto createNewOrder(OrderDto orderDto, String params) throws Exception {

        ParamsModerator paramsModerator = new ParamsModerator(params, orderDto);

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(newOrder, POST, paramsModerator);
            LOGGER.info("New Order information: " + exchangeHttpResponse);

            Gson gson = new Gson();
            CreatedOrderDto createdOrderDto = gson.fromJson(exchangeHttpResponse.getContent(), CreatedOrderDto.class);

            return createdOrderDto;
        } catch (IOException e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        } catch (NullPointerException e) {
            LOGGER.error(ExchangeConnectionExceptions.RETURN_NULL_AFTER_PLACING_ORDER.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.RETURN_NULL_AFTER_PLACING_ORDER.getException(), e);
        }
    }
}
