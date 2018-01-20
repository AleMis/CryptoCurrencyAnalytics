package crypto_analytics.controller.bitfinex;


import crypto_analytics.domain.bitfinex.order.CreatedOrderDto;
import crypto_analytics.domain.bitfinex.order.OrderDto;
import crypto_analytics.facade.bitfinex.OrderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("v1/crypto")
public class NewOrderController {

    @Autowired
    private OrderFacade orderFacade;

    @RequestMapping(method = RequestMethod.POST, value = "/order")
    public CreatedOrderDto createNewOrder(@RequestBody OrderDto orderDto) throws Exception {
        return orderFacade.createNewOrder(orderDto);
    }
}
