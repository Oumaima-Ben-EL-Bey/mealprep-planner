package de.gruppe1.mealprep.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MealDbMeal(
        String idMeal,
        String strMeal,
        String strMealThumb,
        String strCategory,
        String strCountry
) {
}