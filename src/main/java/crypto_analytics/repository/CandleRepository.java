package crypto_analytics.repository;


import crypto_analytics.domain.candle.Candle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface CandleRepository extends CrudRepository<Candle, Long>{

    @Override
    Candle save(Candle candle);

    @Override
    List<Candle> findAll();

    @Query(nativeQuery = true)
    Timestamp getLastDateForCurrency(@Param("CURRENCYPAIR")String currencyPair, @Param("TIMEFRAME") String timeFrame);

}
