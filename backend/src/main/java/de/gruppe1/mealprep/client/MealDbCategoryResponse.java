package de.gruppe1.mealprep.client;

import java.util.List;

public record MealDbCategoryResponse(
        List<MealDbCategory> categories
) {
}