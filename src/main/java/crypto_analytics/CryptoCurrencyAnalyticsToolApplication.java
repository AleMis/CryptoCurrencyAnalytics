package crypto_analytics;


import crypto_analytics.authentication.ExchangeAuthentication;
import crypto_analytics.authentication.ExchangeHttpResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;


@SpringBootApplication
//public class CryptoCurrencyAnalyticsToolApplication extends SpringBootServletInitializer {
public class CryptoCurrencyAnalyticsToolApplication {

	public static void main(String[] args) throws IOException {

		SpringApplication.run(CryptoCurrencyAnalyticsToolApplication.class, args);
	}

	//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(CryptoCurrencyAnalyticsToolApplication.class);
//	}
}
