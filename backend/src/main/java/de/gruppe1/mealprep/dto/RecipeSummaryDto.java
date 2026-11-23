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
    public RecipeSummaryDto withCategory(String category) {
        return new RecipeSummaryDto(id, externalId, title, imageUrl, category, region, favorite);
    }
}