package crypto_analytics.controller;


import crypto_analytics.client.useraccount.*;
import crypto_analytics.domain.accountbalance.AccountBalanceHistoryListDto;
import crypto_analytics.domain.accountbalance.AccountBalanceListDto;
import crypto_analytics.domain.order.CancelAllOrders;
import crypto_analytics.domain.order.CreatedOrderDto;
import crypto_analytics.domain.order.OrderDto;
import crypto_analytics.domain.params.Params;
import crypto_analytics.domain.params.ParamsToSearch;
import crypto_analytics.domain.pasttrades.PastTradesListDto;
import crypto_analytics.domain.positions.ActivePositionsDto;
import crypto_analytics.facade.OrderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("v1/crypto")
public class AccountManagementController {

    @Autowired
    private AccountBalanceClient accountBalanceClient;

    @Autowired
    private AccountBalanceHistoryClient accountBalanceHistoryClient;

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private PastTradesClient pastTradesClient;

    @Autowired
    private CancelOrderClient cancelOrderClient;

    @Autowired
    private CancelAllOrdersClient cancelAllOrdersClient;

    @Autowired
    private ActiveOrdersClient activeOrdersClients;

    @Autowired
    private OrdersHistoryClient ordersHistoryClient;

    @Autowired
    private OrderStatusClient orderStatusClient;

    @Autowired
    private ActivePositionsClient activePositionsClient;

    @RequestMapping(method = RequestMethod.GET, value = "/balance")
    private AccountBalanceListDto getUserAccountBalance() throws Exception {
        return accountBalanceClient.getAccountBalance(Params.WITHOUT_PARAMS.getParams());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/balance-history")
    private AccountBalanceHistoryListDto getUserAccountBalanceHistory(@RequestParam String currency,
                                                                      @RequestParam(required = false) Long since,
                                                                      @RequestParam(required = false) Long until,
                                                                      @RequestParam(required = false) String wallet) throws Exception {

        ParamsToSearch paramsToSearch = new ParamsToSearch(currency, since, until, wallet);
        return accountBalanceHistoryClient.getAccountBalanceHistory(paramsToSearch, Params.BALANCE_HISTORY.getParams());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/order")
    public CreatedOrderDto createNewOrder(@RequestBody OrderDto orderDto) throws Exception {
        return orderFacade.createNewOrder(orderDto);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/past-trades")
    public PastTradesListDto getPastTrades(@RequestParam String symbol, @RequestParam(required = false) String sinceTimestamp) throws Exception {
        return pastTradesClient.getPastTrades(symbol, sinceTimestamp);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cancel-order")
    public boolean cancelOrder(@RequestParam String orderId) throws Exception {
        return cancelOrderClient.cancelOrder(orderId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cancel-allorders")
    public CancelAllOrders cancelMultipleOrders() throws Exception {
        return cancelAllOrdersClient.cancelAllOrders();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orders")
    public CreatedOrderDto[] getActiveOrders() throws Exception {
        return activeOrdersClients.getActiveOrders();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orders-history")
    public CreatedOrderDto[] getOrdersHistory() throws Exception {
        return ordersHistoryClient.getOrdersHistory();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/order-status")
    public CreatedOrderDto getOrderStatus(@RequestParam String orderId) throws Exception {
        return orderStatusClient.getOrdersStatus(orderId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/positions")
    public ActivePositionsDto[] getActivePositions() throws Exception {
        return activePositionsClient.getActivePositions();
    }
}
