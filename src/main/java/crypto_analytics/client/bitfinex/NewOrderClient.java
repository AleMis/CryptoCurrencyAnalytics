package crypto_analytics.client.bitfinex;

import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeConnectionExceptions;
import crypto_analytics.authentication.ExchangeHttpResponse;
import crypto_analytics.converter.bitfinex.OrderConverter;
import crypto_analytics.domain.bitfinex.order.CreatedOrderDto;
import crypto_analytics.domain.bitfinex.order.OrderDto;
import crypto_analytics.domain.bitfinex.params.ParamsModerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class NewOrderClient {

    @Value("${bitfinex.order.new}")
    private String newOrder;

    @Autowired
    private ExchangeAuthentication exchangeAuthentication;

    @Autowired
    private OrderConverter orderConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPermissionsClient.class);

    private static final String POST = "POST";

    public CreatedOrderDto createNewOrder(OrderDto orderDto, String params) throws Exception {

        ParamsModerator paramsModerator = new ParamsModerator(params, orderDto);

        try {
            ExchangeHttpResponse exchangeHttpResponse = exchangeAuthentication.sendExchangeRequest(newOrder, POST, paramsModerator);

            LOGGER.info("New Order information: " + exchangeHttpResponse);

            InputStream inputStream = new ByteArrayInputStream(exchangeHttpResponse.getContent());

            return orderConverter.readCreatedOrder(inputStream);
        } catch (IOException e) {
            LOGGER.error(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.UNEXPECTED_IO_ERROR_MSG.getException(), e);
        } catch (NullPointerException e) {
            LOGGER.error(ExchangeConnectionExceptions.RETURN_NULL_AFTER_PLACING_ORDER.getException(), e);
            throw new IOException(ExchangeConnectionExceptions.RETURN_NULL_AFTER_PLACING_ORDER.getException(), e);
        }
    }
}
