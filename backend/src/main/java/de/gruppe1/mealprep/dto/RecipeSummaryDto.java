package de.gruppe1.mealprep.dto;

/**
 * Eine Rezeptkarte (Discover-Liste, Sammlung).
 * id ist null, solange das Rezept nur bei TheMealDB liegt und nicht bei uns gespeichert ist.
 */
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
    public RecipeSummaryDto withTitle(String title) {
        return new RecipeSummaryDto(id, externalId, title, imageUrl, category, region, favorite);
    }
}