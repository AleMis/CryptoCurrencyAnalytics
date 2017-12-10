package crypto_analytics.service.bitfinex;

import crypto_analytics.domain.bitfinex.books.Books;
import crypto_analytics.domain.bitfinex.candle.Candle;
import crypto_analytics.domain.bitfinex.dbupdater.DbUpdater;
import crypto_analytics.repository.BooksRepository;
import crypto_analytics.repository.CandleRepository;
import crypto_analytics.repository.DbUpdaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class DbService {

    @Autowired
    private CandleRepository candleRepository;

    @Autowired
    private DbUpdaterRepository dbUpdaterRepository;

    @Autowired
    private BooksRepository booksRepository;

    public Candle saveCandle(final Candle candle) {
        return candleRepository.save(candle);
    }

    public List<DbUpdater> getDbUpdaterList() {
        return dbUpdaterRepository.findAll();
    }

    public DbUpdater saveDbUpdater(final DbUpdater dbUpdater) {
        return dbUpdaterRepository.save(dbUpdater);
    }

    public DbUpdater getDbUpdaterByCurrencyPairAndTimeFrame(final String currencyPair, final String timeFrame) {
        return dbUpdaterRepository.findByCurrencyPairAndTimeFrame(currencyPair, timeFrame);
    }

    public List<Candle> getCandlesByCurrencyPairAndTimeFrame(String currencyPair, String timeFrame) {
        return candleRepository.getCandlesByCurrencyPairAndTimeFrame(currencyPair, timeFrame);
    }

    public Books saveBooks(Books books) {
        return booksRepository.save(books);
    }

    public void deleteBooksByCurrencyPair(final String currencyPair) {
        booksRepository.deleteBooksByCurrencyPair(currencyPair);
    }

    public List<Books> getBooksByCurrencyPair(final String currencyPair) {
        return booksRepository.findBooksByCurrencyPair(currencyPair);
    }



}