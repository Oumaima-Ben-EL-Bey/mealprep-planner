package de.gruppe1.mealprep.client;

import java.util.List;
import java.util.Map;

public record MealDbDetailResponse(List<Map<String, String>> meals) {
}