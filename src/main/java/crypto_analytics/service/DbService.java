package crypto_analytics.service;

import crypto_analytics.domain.candle.Candle;
import crypto_analytics.domain.dbupdater.DbUpdater;
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

    public Candle saveCandle(final Candle candle) {
        return candleRepository.save(candle);
    }

    public List<DbUpdater> getDbUpdaterList() {
        return dbUpdaterRepository.findAll();
    }

    public DbUpdater saveDbUpdater(final DbUpdater dbUpdater) {
        return dbUpdaterRepository.save(dbUpdater);
    }

    public List<Candle> getCandlesByCurrencyPairAndTimeFrame(String currencyPair, String timeFrame) {
        return candleRepository.getCandlesByCurrencyPairAndTimeFrame(currencyPair, timeFrame);
    }
}
