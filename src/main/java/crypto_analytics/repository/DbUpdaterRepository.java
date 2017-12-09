package crypto_analytics.repository;

import crypto_analytics.domain.bitfinex.dbupdater.DbUpdater;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DbUpdaterRepository extends CrudRepository<DbUpdater, Long> {

    @Override
    List<DbUpdater> findAll();

    @Override
    DbUpdater save(DbUpdater dbUpdater);

    DbUpdater findByCurrencyPairAndTimeFrame(String currencyPair, String timeFrame);

}
