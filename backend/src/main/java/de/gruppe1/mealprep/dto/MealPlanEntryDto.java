package de.gruppe1.mealprep.dto;

import de.gruppe1.mealprep.entity.MealType;

import java.time.DayOfWeek;

public record MealPlanEntryDto(Long id, DayOfWeek day, MealType mealType, RecipeSummaryDto recipe) {
}
