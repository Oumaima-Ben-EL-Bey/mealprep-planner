package de.gruppe1.mealprep.client;

import de.gruppe1.mealprep.exception.ExternalApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

@Component
public class MealDbClient {

    private final RestClient restClient;

    public MealDbClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<MealDbMeal> searchByName(String term) {
        MealDbResponse response;
        try {
            response = restClient.get()
                    .uri("/search.php?s={term}", term)
                    .retrieve()
                    .body(MealDbResponse.class);
        } catch (RestClientException e) {
            throw new ExternalApiException("TheMealDB ist nicht erreichbar", e);
        }
        if (response == null || response.meals() == null) {
            return List.of();
        }
        return response.meals();
    }
}