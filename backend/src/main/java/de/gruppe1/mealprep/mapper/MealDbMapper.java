package de.gruppe1.mealprep.mapper;

import de.gruppe1.mealprep.client.MealDbMeal;
import de.gruppe1.mealprep.dto.RecipeSummaryDto;
import org.springframework.stereotype.Component;

@Component
public class MealDbMapper {
    public RecipeSummaryDto toSummaryDto(MealDbMeal meal) {
        return new RecipeSummaryDto(null, meal.idMeal(), meal.strMeal(), meal.strMealThumb(), meal.strCategory(), meal.strCountry(), false);
    }
}