package crypto_analytics.controller;

import crypto_analytics.domain.tickers.Tickers;
import crypto_analytics.domain.tickers.TickersDto;
import crypto_analytics.mapper.TickersMapper;
import crypto_analytics.service.DbService;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(TickersController.class)
public class TickersControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService service;

    @MockBean
    private TickersMapper tickersMapper;

    @Test
    public void testGetTickers() throws Exception {
        //Given
        List<Tickers> tickersList = new ArrayList<>();
        tickersList.add(new Tickers(1L, "tBTCUSD", 15000.00, 50.0, 15050.00, 100.00, 200.00, 0.02, 15025.00, 1000.00, 16000.00, 14500.00));
        List<TickersDto> tickersDtoList = new ArrayList<>();
        tickersDtoList.add(new TickersDto("tBTCUSD", 15000.00, 50.0, 15050.00, 100.00, 200.00, 0.02, 15025.00, 1000.00, 16000.00, 14500.00));

        when(service.getAllTickers()).thenReturn(tickersList);
        when(tickersMapper.mapTickersListToTickersDtoList(tickersList)).thenReturn(tickersDtoList);

        //When & Then
        mockMvc.perform(get("/v1/crypto/getTickers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].currencyPair", is("tBTCUSD")))
                .andExpect(jsonPath("$[0].bid", is(15000.00)))
                .andExpect(jsonPath("$[0].bidSize", is(50.00)));
    }
}
