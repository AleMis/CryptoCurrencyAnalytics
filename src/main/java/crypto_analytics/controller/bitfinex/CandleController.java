package crypto_analytics.controller.bitfinex;


import crypto_analytics.domain.bitfinex.candle.CandleChartDto;
import crypto_analytics.mapper.bitfinex.CandleMapper;
import crypto_analytics.service.bitfinex.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("v1/crypto")
public class CandleController {

    @Autowired
    private DbService service;

    @Autowired
    private CandleMapper candleMapper;

    @RequestMapping(method = RequestMethod.GET, value = "getCandles")
    public CandleChartDto[] getCandleList(@RequestParam String currencyPair, @RequestParam String timeFrame) {
        System.out.println(currencyPair + " " + timeFrame);
        return candleMapper.mapToCandleDtoChartsList(service.getCandlesByCurrencyPairAndTimeFrame(currencyPair, timeFrame));
    }
}
