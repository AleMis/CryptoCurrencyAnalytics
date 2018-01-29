package crypto_analytics.mapper;

import crypto_analytics.domain.apikey.ApiKeys;
import crypto_analytics.domain.apikey.ApiKeysDto;
import org.springframework.stereotype.Component;

@Component
public class ApiKeysMapper {

    public ApiKeysDto mapApiKeysToApiKeysDto(ApiKeys apiKeys) {
        return new ApiKeysDto(apiKeys.getApiKey(), apiKeys.getApiSecretKey());
    }
}
