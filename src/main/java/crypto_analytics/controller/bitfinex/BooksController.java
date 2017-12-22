package crypto_analytics.controller.bitfinex;

import crypto_analytics.domain.bitfinex.books.*;
import crypto_analytics.mapper.bitfinex.BooksMapper;
import crypto_analytics.service.bitfinex.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("v1/crypto")
public class BooksController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BooksController.class);

    @Autowired
    private DbService service;

    @Autowired
    private BooksMapper booksMapper;

    @RequestMapping(method=RequestMethod.GET, value="getBooks")
    public MarketValues getBooksData(@RequestParam String currencyPair) {
        LOGGER.info("Get books for " + currencyPair);
        return booksMapper.mapBooksListToBooksChartDto(service.getBooksByCurrencyPair(currencyPair));
    }
}
