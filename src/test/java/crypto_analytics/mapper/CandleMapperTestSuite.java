package crypto_analytics.mapper;

import crypto_analytics.domain.candle.CandleKeyParameters;
import crypto_analytics.domain.candle.Candle;
import crypto_analytics.domain.candle.CandleChartDto;
import crypto_analytics.domain.candle.CandleDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CandleMapperTestSuite {

    @InjectMocks
    private CandleMapper candleMapper;

    @Test
    public void testMapToCandleDtoListForDownload() {
        //Given
        CandleKeyParameters candleKeyParameters = new CandleKeyParameters("tBTCUSD", "1D");
        Object[][] downloadedCandlesData = new Object[1][6];
        downloadedCandlesData[0][0] = 1512777600000L;
        downloadedCandlesData[0][1] = 15000.0;
        downloadedCandlesData[0][2] =  14500.0;
        downloadedCandlesData[0][3] =  16300.0;
        downloadedCandlesData[0][4] = 13010.0;
        downloadedCandlesData[0][5] = 7000.0;

        List<Object[][]> listOfDownloadedCandlesData = new ArrayList<>();
        listOfDownloadedCandlesData.add(downloadedCandlesData);

        HashMap<CandleKeyParameters, List<Object[][]>> candlesData = new HashMap<>();
        candlesData.put(candleKeyParameters, listOfDownloadedCandlesData);

        //When
        List<CandleDto> candleDtoList = candleMapper.mapToCandleDtoListForDownload(candlesData);

        //Then
        assertEquals(1, candleDtoList.size());
        assertEquals("tBTCUSD", candleDtoList.get(0).getCurrencyPair());
        assertEquals(1512777600000L, candleDtoList.get(0).getTimeStamp(), 0);
        assertEquals(15000.0, candleDtoList.get(0).getOpen(), 0);
        assertEquals(14500.0, candleDtoList.get(0).getClose(), 0);
        assertEquals(16300.0, candleDtoList.get(0).getHigh(), 0);
        assertEquals(13010.0, candleDtoList.get(0).getLow(), 0);
        assertEquals(7000.0, candleDtoList.get(0).getVolume(), 0);
    }

    @Test
    public void testMapToCandleDtoListForUpdate() {
        //Given
        CandleKeyParameters candleKeyParameters = new CandleKeyParameters("tBTCUSD", "1D");
        Object[][] downloadedCandlesData = new Object[1][6];
        downloadedCandlesData[0][0] = 1512777600000L;
        downloadedCandlesData[0][1] = 15000.0;
        downloadedCandlesData[0][2] =  14500.0;
        downloadedCandlesData[0][3] =  16300.0;
        downloadedCandlesData[0][4] = 13010.0;
        downloadedCandlesData[0][5] = 7000.0;

        HashMap<CandleKeyParameters, Object[][]> candlesData = new HashMap<>();
        candlesData.put(candleKeyParameters, downloadedCandlesData);

        //When
        List<CandleDto> candleDtoList = candleMapper.mapToCandleDtoListForUpdate(candlesData);

        //Then
        assertEquals(1, candleDtoList.size());
        assertEquals("tBTCUSD", candleDtoList.get(0).getCurrencyPair());
        assertEquals(1512777600000L, candleDtoList.get(0).getTimeStamp(), 0);
        assertEquals(15000.0, candleDtoList.get(0).getOpen(), 0);
        assertEquals(14500.0, candleDtoList.get(0).getClose(), 0);
        assertEquals(16300.0, candleDtoList.get(0).getHigh(), 0);
        assertEquals(13010.0, candleDtoList.get(0).getLow(), 0);
        assertEquals(7000.0, candleDtoList.get(0).getVolume(), 0);
    }

    @Test
    public void testMapToCandle() {
        //Given
        CandleDto candleDto = new CandleDto( 1512777600000L, "tBTCUSD", 15000.0, 14500.0, 16300.0, 13010.0, 7000.0);
        String timeFrame = "1D";

        //When
        Candle candle = candleMapper.mapToCandle(candleDto, timeFrame);

        //Then
        assertEquals("tBTCUSD", candle.getCurrencyPair());
        assertEquals(1512777600000L, candle.getTimeStamp(), 0);
        assertEquals(15000.0, candle.getOpen(), 0);
        assertEquals(14500.0, candle.getClose(), 0);
        assertEquals(16300.0, candle.getHigh(), 0);
        assertEquals(13010.0, candle.getLow(), 0);
        assertEquals(7000.0, candle.getVolume(), 0);
    }

    @Test
    public void testMapToCandleDtoChartsList() {
        //Given
        Candle candle = new Candle(1L, "tBTCUSD", 1512777600000L, "2017-12-09", "01:00", 15000.0, 14500.0, 16300.0, 13010.0, 7000.0, "1D");
        List<Candle> candleList = new ArrayList<>();
        candleList.add(candle);

        //When
        CandleChartDto[] candleChartDto = candleMapper.mapToCandleDtoChartsList(candleList);

        //Then
        assertEquals(1512777600000L, candleChartDto[0].getDate(), 0);
        assertEquals(15000.0, candleChartDto[0].getOpen(), 0);
        assertEquals(14500.0, candleChartDto[0].getClose(), 0);
        assertEquals(16300.0, candleChartDto[0].getHigh(), 0);
        assertEquals(13010.0, candleChartDto[0].getLow(), 0);
        assertEquals(7000.0, candleChartDto[0].getVolume(), 0);
    }
}
