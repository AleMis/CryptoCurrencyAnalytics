package crypto_analytics.repository;

import crypto_analytics.domain.apikey.ApiKeys;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ApiKeyRepository extends CrudRepository<ApiKeys, Long>{

    ApiKeys findAllById(Long id);
}
