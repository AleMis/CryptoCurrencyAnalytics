package crypto_analytics.controller;


import crypto_analytics.domain.candle.CandleDto;
import crypto_analytics.domain.candle.CandleDtoCharts;
import crypto_analytics.mapper.CandleMapper;
import crypto_analytics.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("v1/crypto")
public class CandleRestController {

    @Autowired
    private DbService service;

    @Autowired
    private CandleMapper candleMapper;


    @RequestMapping(method = RequestMethod.GET, value = "getCandles")
    public CandleDtoCharts[] getCandleList(@RequestParam String currencyPair, @RequestParam String timeFrame) {
        return candleMapper.mapToCandleDtoChartsList(service.getCandlesByCurrencyPairAndTimeFrame(currencyPair, timeFrame));
    }



}
