package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.client.MealDbClient;
import de.gruppe1.mealprep.dto.IngredientDto;
import de.gruppe1.mealprep.dto.RecipeDetailDto;
import de.gruppe1.mealprep.dto.RecipeSummaryDto;
import de.gruppe1.mealprep.exception.ExternalApiException;
import de.gruppe1.mealprep.exception.NotFoundException;
import de.gruppe1.mealprep.mapper.MealDbMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalRecipeService {
    private final MealDbClient mealDbClient;
    private final MealDbMapper mealDbMapper;
    private final TranslationService translationService;

    public ExternalRecipeService(MealDbClient mealDbClient, MealDbMapper mealDbMapper, TranslationService translationService) {
        this.mealDbClient = mealDbClient;
        this.mealDbMapper = mealDbMapper;
        this.translationService = translationService;
    }
    public List<RecipeSummaryDto> search(String term, String category) {
        String englishTerm = term.isBlank() ? term : translationService.toEnglish(term);
        List<RecipeSummaryDto> results = searchInEnglish(englishTerm, category);

        List<String> germanTitles = translationService.toGerman(
                results.stream().map(RecipeSummaryDto::title).toList());
        List<RecipeSummaryDto> translated = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            translated.add(results.get(i).withTitle(germanTitles.get(i)));
        }
        return translated;
    }

    private List<RecipeSummaryDto> searchInEnglish(String term, String category) {
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
    public RecipeDetailDto getDetailsInGerman(String mealDbId) {
        RecipeDetailDto dto = getDetails(mealDbId);

        List<String> texts = new ArrayList<>();
        texts.add(dto.title());
        texts.add(dto.instructions());
        for (IngredientDto ingredient : dto.ingredients()) {
            texts.add(ingredient.name());
            texts.add(ingredient.measure());
        }

        List<String> german = translationService.toGerman(texts);

        List<IngredientDto> ingredients = new ArrayList<>();
        for (int i = 0; i < dto.ingredients().size(); i++) {
            ingredients.add(new IngredientDto(german.get(2 + i * 2), german.get(3 + i * 2)));
        }
        return new RecipeDetailDto(dto.id(), dto.externalId(), german.get(0), dto.category(),
                dto.region(), german.get(1), dto.imageUrl(), ingredients, dto.favorite());
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