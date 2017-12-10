package crypto_analytics.repository;

import crypto_analytics.domain.bitfinex.books.Books;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface BooksRepository extends CrudRepository<Books, Long> {

    @Override
    Books save(Books books);

    @Modifying
    @Query
    void deleteBooksByCurrencyPair(@Param("CURRENCY_PAIR") String currencyPair);

    List<Books> findBooksByCurrencyPair(String currencyPair);
}
