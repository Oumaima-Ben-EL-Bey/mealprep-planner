package de.gruppe1.mealprep.mapper;

import de.gruppe1.mealprep.dto.RecipeDetailDto;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MealDbMapperTest {

    @Test
    void toDetailDto_SkipsEmptyIngredientsAndTrims() {
        Map<String, String> meal = new HashMap<>();
        meal.put("idMeal", "1");
        meal.put("strMeal", "Test");
        meal.put("strIngredient1", "Sugar");
        meal.put("strMeasure1", "100g ");
        meal.put("strIngredient2", "");
        meal.put("strMeasure2", "");
        meal.put("strIngredient3", null);

        RecipeDetailDto dto = new MealDbMapper().toDetailDto(meal);

        assertEquals(1, dto.ingredients().size());
        assertEquals("Sugar", dto.ingredients().get(0).name());
        assertEquals("100g", dto.ingredients().get(0).measure());
    }
}