package de.gruppe1.mealprep.mapper;

import de.gruppe1.mealprep.client.MealDbMeal;
import de.gruppe1.mealprep.dto.IngredientDto;
import de.gruppe1.mealprep.dto.RecipeDetailDto;
import de.gruppe1.mealprep.dto.RecipeSummaryDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MealDbMapper {
    public RecipeSummaryDto toSummaryDto(MealDbMeal meal) {
        return new RecipeSummaryDto(null, meal.idMeal(), meal.strMeal(), meal.strMealThumb(), meal.strCategory(), meal.strCountry(), false);
    }

    public RecipeDetailDto toDetailDto(Map<String, String> meal) {
        List<IngredientDto> ingredients = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String name = meal.get("strIngredient" + i);
            if (name == null || name.isBlank()) {
                continue;
            }
            String measure = meal.get("strMeasure" + i);
            ingredients.add(new IngredientDto(name.trim(), measure == null ? "" : measure.trim()));
        }
        return new RecipeDetailDto(
                null,
                meal.get("idMeal"),
                meal.get("strMeal"),
                meal.get("strCategory"),
                meal.get("strCountry"),
                meal.get("strInstructions"),
                meal.get("strMealThumb"),
                ingredients,
                false
        );
    }

}