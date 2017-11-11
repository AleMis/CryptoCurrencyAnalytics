package crypto_analytics.service;

import crypto_analytics.domain.symbol.Symbol;
import crypto_analytics.repository.SymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DbService {

    @Autowired
    private SymbolRepository symbolRepository;

    public Set<Symbol> getSymbols() {
        return symbolRepository.findAll();
    }
}
