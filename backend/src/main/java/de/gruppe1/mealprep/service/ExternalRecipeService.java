package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.client.MealDbClient;
import de.gruppe1.mealprep.client.MealDbMeal;
import de.gruppe1.mealprep.dto.IngredientDto;
import de.gruppe1.mealprep.dto.RandomMode;
import de.gruppe1.mealprep.dto.RecipeDetailDto;
import de.gruppe1.mealprep.dto.RecipeSummaryDto;
import de.gruppe1.mealprep.entity.PantryItem;
import de.gruppe1.mealprep.exception.ExternalApiException;
import de.gruppe1.mealprep.exception.NotFoundException;
import de.gruppe1.mealprep.mapper.MealDbMapper;
import de.gruppe1.mealprep.repository.PantryItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExternalRecipeService {
    private final MealDbClient mealDbClient;
    private final MealDbMapper mealDbMapper;
    private final TranslationService translationService;
    private final PantryItemRepository pantryItemRepository;

    public ExternalRecipeService(MealDbClient mealDbClient, MealDbMapper mealDbMapper, TranslationService translationService, PantryItemRepository pantryItemRepository) {
        this.mealDbClient = mealDbClient;
        this.mealDbMapper = mealDbMapper;
        this.translationService = translationService;
        this.pantryItemRepository = pantryItemRepository;
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
    public RecipeDetailDto getRandom(RandomMode mode, String category) {
        return switch (mode) {
            case ADVENTURE -> getRandom();
            case CATEGORY -> category.isBlank() ? getRandom() : randomFrom(mealDbClient.filterByCategory(category));
            case PANTRY -> randomFromPantry();
        };
    }

    // Ein zufälliges Rezept aus der Liste ziehen und seine Details laden
    private RecipeDetailDto randomFrom(List<MealDbMeal> meals) {
        if (meals.isEmpty()) {
            throw new NotFoundException("Dazu wurde kein Rezept gefunden.");
        }
        MealDbMeal meal = meals.get(ThreadLocalRandom.current().nextInt(meals.size()));
        return getDetails(meal.idMeal());
    }

    // Vorräte in zufälliger Reihenfolge durchgehen, bis eine Zutat bei TheMealDB Treffer hat
    private RecipeDetailDto randomFromPantry() {
        List<String> names = new ArrayList<>(pantryItemRepository.findAll().stream().map(PantryItem::getName).toList());
        if (names.isEmpty()) {
            throw new NotFoundException("Dein Vorrat ist leer. Lege zuerst ein paar Vorräte an.");
        }
        Collections.shuffle(names);
        for (String name : names.subList(0, Math.min(5, names.size()))) {
            // TheMealDB erwartet englische Namen mit Unterstrich, z. B. "chicken_breast"
            String english = translationService.toEnglish(name).trim().toLowerCase(Locale.ROOT).replace(' ', '_');
            List<MealDbMeal> meals = mealDbClient.filterByIngredient(english);
            if (!meals.isEmpty()) {
                return randomFrom(meals);
            }
        }
        throw new NotFoundException("Zu deinen Vorräten wurde kein passendes Rezept gefunden.");
    }
}