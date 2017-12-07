package crypto_analytics.repository;


import crypto_analytics.domain.bitfinex.candle.Candle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CandleRepository extends CrudRepository<Candle, Long>{

    @Override
    Candle save(Candle candle);

    @Override
    List<Candle> findAll();

    @Query(nativeQuery = true)
    Optional<Long> getLastDateForCurrency(@Param("CURRENCY_PAIR")String currencyPair, @Param("TIME_FRAME") String timeFrame);

    @Query(nativeQuery = true)
    Long checkFirstDate(@Param("CURRENCY_PAIR") String currencyPair, @Param("TIME_FRAME") String timeFrame, @Param("TIME_STAMP") Long timeStamp);

    @Query(nativeQuery = true)
    List<Candle> getCandlesByCurrencyPairAndTimeFrame(@Param("CURRENCY_PAIR") String currencyPair, @Param("TIME_FRAME") String timeFrame);

}
