package crypto_analytics.mapper.bitfinex;

import crypto_analytics.domain.bitfinex.books.Books;
import crypto_analytics.domain.bitfinex.books.BooksDto;
import crypto_analytics.domain.bitfinex.books.MarketValues;
import crypto_analytics.mapper.bitfinex.BooksMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class BooksMapperTestSuite {

    @InjectMocks
    private BooksMapper booksMapper;

    @Test
    public void testMapBooksDtoListToBooksList() {
        //Given
        String currencyPair = "tBTCUSD";
        BooksDto booksDto = new BooksDto(15000.0, 1, 2000.0);

        List<BooksDto> booksDtoList = new ArrayList<>();
        booksDtoList.add(booksDto);

        HashMap<String, List<BooksDto>> booksData = new HashMap<>();
        booksData.put(currencyPair, booksDtoList);

        //When
        List<Books> booksList = booksMapper.mapBooksDtoListToBooksList(booksData);

        //Then
        assertEquals(1, booksList.size());
        assertEquals("tBTCUSD", booksList.get(0).getCurrencyPair());
        assertEquals(15000.0, booksList.get(0).getPrice(), 0);
        assertEquals(1, booksList.get(0).getCount(), 0);
        assertEquals(2000.0, booksList.get(0).getAmount(), 0);
    }

    @Test
    public void testMapToBooksDtoList() {
        //Given
        String currencyPair = "tBTCUSD";
        Object[][] booksData = new Object[1][3];
        booksData[0][0] = 15000.0;
        booksData[0][1] = 1;
        booksData[0][2] = 2000.0;

        HashMap<String, Object[][]> booksDataMap = new HashMap<>();
        booksDataMap.put(currencyPair, booksData);

        //When
        HashMap<String, List<BooksDto>> mappedBooksData = booksMapper.mapToBooksDtoList(booksDataMap);

        //Then
        assertEquals(1, mappedBooksData.size());
        assertTrue(mappedBooksData.containsKey(currencyPair));
        assertEquals(15000.0, mappedBooksData.entrySet().iterator().next().getValue().get(0).getPrice(), 0);
        assertEquals(1, mappedBooksData.entrySet().iterator().next().getValue().get(0).getCount(), 0);
        assertEquals(2000.0, mappedBooksData.entrySet().iterator().next().getValue().get(0).getAmount(), 0);
    }

    @Test
    public void testMapBooksListToBooksChartDto() {
        //Given
        List<Books> booksList = new ArrayList<>();
        booksList.add(new Books(1L, "tBTCUSD", 15000.00, 1, 200.00));
        booksList.add(new Books(2L, "tBTCUSD", 16000.00, 2, -100.00));

        //When
        MarketValues marketValues = booksMapper.mapBooksListToMarketValues(booksList);

        //Then
        assertEquals("16000.0", marketValues.getAsks()[0][0]);
        assertEquals("100.0", marketValues.getAsks()[0][1]);
        assertEquals("15000.0", marketValues.getBids()[0][0]);
        assertEquals("200.0", marketValues.getBids()[0][1]);
    }
}
