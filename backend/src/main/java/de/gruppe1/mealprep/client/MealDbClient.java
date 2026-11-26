package de.gruppe1.mealprep.client;

import de.gruppe1.mealprep.exception.ExternalApiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MealDbClient {

    private final RestClient restClient;

    public MealDbClient(@Qualifier("mealDbRestClient") RestClient restClient) {this.restClient = restClient;
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

    public List<MealDbMeal> filterByCategory(String category) {
        MealDbResponse response;
        try {
            response = restClient.get()
                    .uri("/filter.php?c={category}", category)
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

    public List<MealDbCategory> listCategories() {
        MealDbCategoryResponse response;
        try {
            response = restClient.get()
                    .uri("/categories.php")
                    .retrieve()
                    .body(MealDbCategoryResponse.class);
        } catch (RestClientException e) {
            throw new ExternalApiException("TheMealDB ist nicht erreichbar", e);
        }
        if (response == null || response.categories() == null) {
            return List.of();
        }
        return response.categories();
    }

    public Optional<Map<String, String>> lookupById(String mealDbId) {
        return fetchSingleMeal("/lookup.php?i={id}", mealDbId);
    }

    public Optional<Map<String, String>> random() {
        return fetchSingleMeal("/random.php");
    }

    private Optional<Map<String, String>> fetchSingleMeal(String uri, Object... uriVariables) {
        MealDbDetailResponse response;
        try {
            response = restClient.get()
                    .uri(uri, uriVariables)
                    .retrieve()
                    .body(MealDbDetailResponse.class);
        } catch (RestClientException e) {
            throw new ExternalApiException("TheMealDB ist nicht erreichbar", e);
        }
        if (response == null || response.meals() == null || response.meals().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(response.meals().get(0));
    }
}