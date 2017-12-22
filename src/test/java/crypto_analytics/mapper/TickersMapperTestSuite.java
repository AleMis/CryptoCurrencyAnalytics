package crypto_analytics.mapper;


import crypto_analytics.domain.bitfinex.tickers.Tickers;
import crypto_analytics.domain.bitfinex.tickers.TickersDto;
import crypto_analytics.mapper.bitfinex.TickersMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TickersMapperTestSuite {

    @InjectMocks
    private TickersMapper tickersMapper;

    @Test
    public void testMapTickersDtoToTickers() {
        //Given
        TickersDto tickersDto = new TickersDto("tBTCUSD", 15000.0, 50.0, 15050.0, 100.0, 200.0, 0.02, 15025.0, 1000.0, 16000.0, 14500.0);

        //When
        Tickers tickers = tickersMapper.mapTickersDtoToTickers(tickersDto);

        //Then
        assertEquals("tBTCUSD", tickers.getCurrencyPair());
        assertEquals(15000.0, tickers.getBid(), 0);
        assertEquals(50.0, tickers.getBidSize(), 0);
        assertEquals(15050.0, tickers.getAsk(), 0);
        assertEquals(100.0, tickers.getAskSize(), 0);
        assertEquals(200.0, tickers.getDailyChange(), 0);
        assertEquals(0.02, tickers.getDailyChangePerc(), 0);
        assertEquals(15025.0, tickers.getLastPrice(), 0);
        assertEquals(1000.0, tickers.getVolume(), 0);
        assertEquals(16000.0, tickers.getHigh(), 0);
        assertEquals(14500.0, tickers.getLow(), 0);
    }

    @Test
    public void testMapTickersArrayToListTickers() {
        //Given
        String[][] tickersData = new String[1][11];
        tickersData[0][0] = "tBTCUSD";
        tickersData[0][1] = "15000.0";
        tickersData[0][2] = "50.0";
        tickersData[0][3] = "15050.0";
        tickersData[0][4] = "100.0";
        tickersData[0][5] = "200.0";
        tickersData[0][6] = "0.02";
        tickersData[0][7] = "15025.0";
        tickersData[0][8] = "1000.0";
        tickersData[0][9] = "16000.0";
        tickersData[0][10] = "14500.0";

        //When
        List<Tickers> tickersList = tickersMapper.mapTickersArrayToListTickers(tickersData);

        //Then
        assertEquals(1, tickersList.size());
        assertEquals("tBTCUSD", tickersList.get(0).getCurrencyPair());
        assertEquals(15000.0, tickersList.get(0).getBid(), 0);
        assertEquals(50.0, tickersList.get(0).getBidSize(), 0);
        assertEquals(15050.0, tickersList.get(0).getAsk(), 0);
        assertEquals(100.0, tickersList.get(0).getAskSize(), 0);
        assertEquals(200.0, tickersList.get(0).getDailyChange(), 0);
        assertEquals(0.02, tickersList.get(0).getDailyChangePerc(), 0);
        assertEquals(15025.0, tickersList.get(0).getLastPrice(), 0);
        assertEquals(1000.0, tickersList.get(0).getVolume(), 0);
        assertEquals(16000.0, tickersList.get(0).getHigh(), 0);
        assertEquals(14500.0, tickersList.get(0).getLow(), 0);
    }

    @Test
    public void testMapTickersListToTickersDtoList() {
        //Given
        List<Tickers> tickersList = new ArrayList<>();
        tickersList.add(new Tickers(1L,"tBTCUSD", 15000.0, 50.0, 15050.0, 100.0, 200.0, 0.02, 15025.0, 1000.0, 16000.0, 14500.0));

        //When
        List<TickersDto> tickersDtoList = tickersMapper.mapTickersListToTickersDtoList(tickersList);

        //Then
        assertEquals(1, tickersDtoList.size());
        assertEquals("tBTCUSD", tickersDtoList.get(0).getCurrencyPair());
    }
}
