package de.gruppe1.mealprep.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient mealDbRestClient(
            RestClient.Builder builder,
            @Value("${mealdb.base-url}") String baseUrl
    ) {
        return builder.baseUrl(baseUrl).build();
    }
    @Bean
    public RestClient deepLRestClient(RestClient.Builder builder,
                                      @Value("${deepl.base-url}") String baseUrl,
                                      @Value("${deepl.api-key}") String apiKey) {
        return builder.clone()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "DeepL-Auth-Key " + apiKey)
                .build();
    }
}