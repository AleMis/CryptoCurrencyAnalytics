package crypto_analytics.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
<<<<<<< HEAD
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
=======
import org.springframework.web.client.RestTemplate;

>>>>>>> e39cef0... Code refactoring: changes in packages, class and methods names, simplifying few methods and class
@Configuration
public class CoreConfiguration {

    @Autowired
    private RestTemplateBuilder builder;

    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }
}
