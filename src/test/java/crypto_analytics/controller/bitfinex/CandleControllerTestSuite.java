package crypto_analytics.controller.bitfinex;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import crypto_analytics.controller.bitfinex.CandleController;
import crypto_analytics.domain.bitfinex.candle.Candle;
import crypto_analytics.domain.bitfinex.candle.CandleChartDto;
import crypto_analytics.mapper.bitfinex.CandleMapper;
import crypto_analytics.service.bitfinex.DbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(CandleController.class)
public class CandleControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService service;

    @MockBean
    private CandleMapper candleMapper;

    @Test
    public void testCandleController() throws Exception {
        //Given
        List<Candle> candleList = new ArrayList<>();
        candleList.add(new Candle(1L, "tBTCUSD", 1512777600000L, "2017-12-09", "01:00", 15000.00, 14500.00, 16300.000000, 13010.00, 7000.00, "1D"));
        String currencyPair = "tBTCUSD";
        String timeFrame = "1D";

        CandleChartDto candleChartDto = new CandleChartDto(1512777600000L, 15000.00, 14500.00, 16300.000000, 13010.00, 7000.00);
        CandleChartDto[] candleChartData = new CandleChartDto[1];
        candleChartData[0] = candleChartDto;

        when(service.getCandlesByCurrencyPairAndTimeFrame(currencyPair,timeFrame)).thenReturn(candleList);
        when(candleMapper.mapToCandleDtoChartsList(candleList)).thenReturn(candleChartData);

        //When & then
        mockMvc.perform(get("/v1/crypto/getCandles")
                .param("currencyPair", "tBTCUSD")
                .param("timeFrame", "1D")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].date", is(1512777600000L)))
                .andExpect(jsonPath("$[0].open", is(15000.00)))
                .andExpect(jsonPath("$[0].close", is(14500.00)));
    }
}
