package de.gruppe1.mealprep.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MealDbCategory(
              String strCategory,
              String strCategoryDescription
) {
}