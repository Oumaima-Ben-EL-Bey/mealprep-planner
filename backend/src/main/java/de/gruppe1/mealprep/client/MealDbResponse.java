package de.gruppe1.mealprep.client;

import java.util.List;

public record MealDbResponse(
        List<MealDbMeal> meals
) {
}