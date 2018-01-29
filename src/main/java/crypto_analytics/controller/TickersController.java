package crypto_analytics.controller;


import crypto_analytics.domain.tickers.TickersDto;
import crypto_analytics.mapper.TickersMapper;
import crypto_analytics.service.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/crypto")
public class TickersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CandleController.class);

    @Autowired
    private DbService service;

    @Autowired
    private TickersMapper tickersMapper;

    @RequestMapping(method= RequestMethod.GET, value="getTickers")
    public List<TickersDto> getTickers() {
        LOGGER.info("Get tickers data");
        return tickersMapper.mapTickersListToTickersDtoList(service.getAllTickers());
    }

}
