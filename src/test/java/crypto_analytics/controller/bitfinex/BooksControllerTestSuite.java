package crypto_analytics.controller.bitfinex;


import crypto_analytics.controller.bitfinex.BooksController;
import crypto_analytics.domain.bitfinex.books.Books;
import crypto_analytics.domain.bitfinex.books.MarketValues;
import crypto_analytics.mapper.bitfinex.BooksMapper;
import crypto_analytics.service.bitfinex.DbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BooksController.class)
public class BooksControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService service;

    @MockBean
    private BooksMapper booksMapper;

    @Test
    public void testGetBooks() throws Exception {
        //Given
        String currencyPair = "tBTCUSD";
        List<Books> booksList = new ArrayList<>();
        booksList.add(new Books(1L, "tBTCUSD", 15000.00, 1, 200.00));
        booksList.add(new Books(2L, "tBTCUSD", 16000.00, 2, -100.00));

        String[][] asks = new String[1][2];
        String[][] bids = new String[1][2];

        asks[0][0] = booksList.get(0).getPrice().toString();
        asks[0][1] = booksList.get(0).getAmount().toString();

        bids[0][0] = booksList.get(1).getPrice().toString();
        bids[0][1] = booksList.get(1).getAmount().toString();

        MarketValues marketValues = new MarketValues(asks, bids);

        when(service.getBooksByCurrencyPair(currencyPair)).thenReturn(booksList);
        when(booksMapper.mapBooksListToMarketValues(booksList)).thenReturn(marketValues);

        //When & Then
        mockMvc.perform(get("/v1/crypto/getBooks")
                .param("currencyPair", "tBTCUSD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asks", hasSize(1)))
                .andExpect(jsonPath("$.bids", hasSize(1)))
                .andExpect(jsonPath("$.asks[0][0]", is("15000.0")))
                .andExpect(jsonPath("$.asks[0][1]", is("200.0")))
                .andExpect(jsonPath("$.bids[0][0]", is("16000.0")))
                .andExpect(jsonPath("$.bids[0][1]", is("-100.0")));
    }
}
