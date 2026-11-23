package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.client.MealDbClient;
import de.gruppe1.mealprep.dto.RecipeSummaryDto;
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

        return List.of();
    }
}