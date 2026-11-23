package de.gruppe1.mealprep.dto;

public record RecipeSummaryDto(
        Long id,
        String externalId,
        String title,
        String imageUrl,
        String category,
        String region,
        boolean favorite
) {
}