package crypto_analytics.service;

import crypto_analytics.domain.candle.Candle;
import crypto_analytics.domain.symbol.Symbol;
import crypto_analytics.repository.CandleRepository;
import crypto_analytics.repository.SymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
public class DbService {

    @Autowired
    private SymbolRepository symbolRepository;

    @Autowired
    private CandleRepository candleRepository;

    public Set<Symbol> getSymbolList() {
        return symbolRepository.findAll();
    }

    public List<Candle> getCandleList() {
        return candleRepository.findAll();
    }

    public Long getLastDateForCurrency(String currencyPair, String timeFrame) {
        return candleRepository.getLastDateForCurrency(currencyPair, timeFrame);
    }

    public Candle saveCandle(final Candle candle) {
        return candleRepository.save(candle);
    }

}
