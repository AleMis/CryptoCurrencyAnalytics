package crypto_analytics.facade.bitfinex;

import crypto_analytics.client.bitfinex.AccountPermissionsClient;
import crypto_analytics.client.bitfinex.NewOrderClient;
import crypto_analytics.domain.bitfinex.order.CreatedOrderDto;
import crypto_analytics.domain.bitfinex.order.OrderDto;
import crypto_analytics.domain.bitfinex.params.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPermissionsClient.class);

    @Autowired
    private NewOrderClient newOrderClient;

    public CreatedOrderDto createNewOrder(final OrderDto orderDto) throws Exception {
        return newOrderClient.createNewOrder(orderDto, Params.NEW_ORDER.getParams());
    }
}
