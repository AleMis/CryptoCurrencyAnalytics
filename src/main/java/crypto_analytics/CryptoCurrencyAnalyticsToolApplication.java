package crypto_analytics;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


@SpringBootApplication
public class CryptoCurrencyAnalyticsToolApplication extends SpringBootServletInitializer {
//public class CryptoCurrencyAnalyticsToolApplication {
	public static void main(String[] args) {
		SpringApplication.run(CryptoCurrencyAnalyticsToolApplication.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(CryptoCurrencyAnalyticsToolApplication.class);
	}
}
