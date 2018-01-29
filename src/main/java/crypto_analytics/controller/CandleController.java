package crypto_analytics.controller;

import crypto_analytics.domain.candle.CandleChartDto;
import crypto_analytics.mapper.CandleMapper;
import crypto_analytics.service.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("v1/crypto")
public class CandleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CandleController.class);

    @Autowired
    private DbService service;

    @Autowired
    private CandleMapper candleMapper;

    @RequestMapping(method = RequestMethod.GET, value="getCandles")
    public CandleChartDto[] getCandles(@RequestParam String currencyPair, @RequestParam String timeFrame) {
        LOGGER.info("Method: getCandles for currency pair: " + currencyPair + " and time frame: " + timeFrame + " was called.");
        return candleMapper.mapToCandleDtoChartsList(service.getCandlesByCurrencyPairAndTimeFrame(currencyPair, timeFrame));
    }
}
