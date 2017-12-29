package crypto_analytics.mapper.bitfinex;

import crypto_analytics.domain.bitfinex.apikey.ApiKeys;
import crypto_analytics.domain.bitfinex.apikey.ApiKeysDto;
import org.springframework.stereotype.Component;

@Component
public class ApiKeysMapper {

    public ApiKeysDto mapApiKeysToApiKeysDto(ApiKeys apiKeys) {
        return new ApiKeysDto(apiKeys.getApiKey(), apiKeys.getApiSecretKey());
    }
}
