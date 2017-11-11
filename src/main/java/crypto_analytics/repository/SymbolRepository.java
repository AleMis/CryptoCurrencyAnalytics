package crypto_analytics.repository;

import crypto_analytics.domain.symbol.Symbol;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface SymbolRepository extends CrudRepository<Symbol, Long> {

    @Override
    Set<Symbol> findAll();
}
