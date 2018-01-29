package crypto_analytics.facade;

import crypto_analytics.client.useraccount.NewOrderClient;
import crypto_analytics.domain.order.CreatedOrderDto;
import crypto_analytics.domain.order.OrderDto;
import crypto_analytics.domain.params.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderFacade {

    @Autowired
    private NewOrderClient newOrderClient;

    public CreatedOrderDto createNewOrder(final OrderDto orderDto) throws Exception {
        return newOrderClient.createNewOrder(orderDto, Params.NEW_ORDER.getParams());
    }
}
