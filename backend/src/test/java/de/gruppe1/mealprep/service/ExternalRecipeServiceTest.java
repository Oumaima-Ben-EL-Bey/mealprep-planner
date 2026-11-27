package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.dto.RecipeDetailDto;
import de.gruppe1.mealprep.dto.RecipeSummaryDto;
import de.gruppe1.mealprep.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ExternalRecipeServiceTest {
    @Autowired
    ExternalRecipeService externalRecipeService;

    @Test
    void search_TermIsBlankCategoryIsSet() {
        List<RecipeSummaryDto> meals = externalRecipeService.search("", "Seafood");

        assertFalse(meals.isEmpty());
        assertTrue(meals.stream().allMatch((m) -> "Seafood".equals(m.category())));
    }

    @Test
    void search_TermAndCategoryAreSet() {
        List<RecipeSummaryDto> meals = externalRecipeService.search("Rice", "Chicken");
        assertFalse(meals.isEmpty());
        assertTrue(meals.stream().allMatch((m) -> "Chicken".equals(m.category())));

    }

    @Test
    void getDetails_KnownIdHasIngredients() {
        RecipeDetailDto dto = externalRecipeService.getDetails("52772");
        assertFalse(dto.ingredients().isEmpty());
    }

    @Test
    void getDetails_UnknownIdThrowsNotFound() {
        assertThrows(NotFoundException.class, () -> externalRecipeService.getDetails("1"));
    }

    @Test
    void getRandom_ReturnsRecipeWithIngredients() {
        RecipeDetailDto dto = externalRecipeService.getRandom();
        assertFalse(dto.ingredients().isEmpty());
    }

    @Test
    void search_GermanTermFindsRecipes() {
        assertFalse(externalRecipeService.search("Hähnchen", "").isEmpty());
    }

    @Test
    void getDetailsInGerman_TranslatesTitleButKeepsIds() {
        RecipeDetailDto english = externalRecipeService.getDetails("52772");
        RecipeDetailDto german = externalRecipeService.getDetailsInGerman("52772");

        assertNotEquals(english.title(), german.title());
        assertEquals(english.externalId(), german.externalId());
        assertEquals(english.ingredients().size(), german.ingredients().size());
    }
}