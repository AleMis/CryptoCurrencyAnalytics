package crypto_analytics.controller.bitfinex;


import crypto_analytics.domain.bitfinex.candle.CandleChartDto;
import crypto_analytics.mapper.bitfinex.CandleMapper;
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
public class CandleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CandleController.class);

    @Autowired
    private DbService service;

    @Autowired
    private CandleMapper candleMapper;

    @RequestMapping(method = RequestMethod.GET, value = "getCandles")
    public CandleChartDto[] getCandleList(@RequestParam String currencyPair, @RequestParam String timeFrame) {
        LOGGER.info("Get candles: " + currencyPair + " : " + timeFrame);
        return candleMapper.mapToCandleDtoChartsList(service.getCandlesByCurrencyPairAndTimeFrame(currencyPair, timeFrame));
    }
}
