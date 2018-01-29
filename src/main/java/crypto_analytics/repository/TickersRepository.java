package crypto_analytics.repository;

import crypto_analytics.domain.tickers.Tickers;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface TickersRepository extends CrudRepository<Tickers, Long>{

    @Override
    Tickers save(Tickers tickers);

    @Modifying
    @Query(nativeQuery = true)
    void deleteAllTickersData();

    @Override
    List<Tickers> findAll();
}
