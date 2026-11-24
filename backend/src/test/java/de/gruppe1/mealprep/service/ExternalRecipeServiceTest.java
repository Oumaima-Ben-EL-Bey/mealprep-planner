package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.dto.RecipeSummaryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}