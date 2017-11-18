package crypto_analytics.repository;

import crypto_analytics.domain.dbupdater.DbUpdater;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DbUpdaterRepository extends CrudRepository<DbUpdater, Long> {

    @Override
    List<DbUpdater> findAll();

    @Override
    DbUpdater save(DbUpdater dbUpdater);

}
