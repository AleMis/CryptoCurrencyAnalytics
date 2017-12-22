package crypto_analytics.controller;


import crypto_analytics.controller.bitfinex.BooksController;
import crypto_analytics.domain.bitfinex.books.Books;
import crypto_analytics.domain.bitfinex.books.MarketValues;
import crypto_analytics.mapper.bitfinex.BooksMapper;
import crypto_analytics.mapper.bitfinex.CandleMapper;
import crypto_analytics.service.bitfinex.DbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

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
    public void testGetBooks() {
        //Given


    }

}
