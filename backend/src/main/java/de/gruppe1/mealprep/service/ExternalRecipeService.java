package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.client.MealDbClient;
import de.gruppe1.mealprep.dto.RecipeDetailDto;
import de.gruppe1.mealprep.dto.RecipeSummaryDto;
import de.gruppe1.mealprep.exception.ExternalApiException;
import de.gruppe1.mealprep.exception.NotFoundException;
import de.gruppe1.mealprep.mapper.MealDbMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalRecipeService {
    private final MealDbClient mealDbClient;
    private final MealDbMapper mealDbMapper;

    public ExternalRecipeService(MealDbClient mealDbClient, MealDbMapper mealDbMapper) {
        this.mealDbClient = mealDbClient;
        this.mealDbMapper = mealDbMapper;
    }

    public List<RecipeSummaryDto> search(String term, String category) {
        if (category.isBlank()) {
            return mealDbClient.searchByName(term)
                    .stream()
                    .map(mealDbMapper::toSummaryDto)
                    .toList();
        }
        if(term.isBlank()) {
            return mealDbClient.filterByCategory(category)
                    .stream()
                    .map(mealDbMapper::toSummaryDto)
                    .map(m-> m.withCategory(category))
                    .toList();
        }


        return mealDbClient.searchByName(term)
                .stream()
                .map(mealDbMapper::toSummaryDto)
                .filter((meal)-> category.equals(meal.category()))
                .toList();
    }
    public RecipeDetailDto getDetails(String mealDbId) {
        return mealDbClient.lookupById(mealDbId)
                .map(mealDbMapper::toDetailDto)
                .orElseThrow(() -> new NotFoundException("Rezept nicht gefunden"));
    }
    public RecipeDetailDto getRandom() {
        return mealDbClient.random()
                .map(mealDbMapper::toDetailDto)
                .orElseThrow(() -> new ExternalApiException("Kein Zufallsrezept erhalten", null));
    }
}