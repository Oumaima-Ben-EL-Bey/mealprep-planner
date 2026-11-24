package de.gruppe1.mealprep.dto;

import java.util.List;

/**
 * Ein vollständiges Rezept für die Detailseite.
 * Das Frontend entscheidet seine Buttons aus id und externalId:
 * id null -> "Speichern", id gesetzt + externalId null -> "Bearbeiten"/"Löschen",
 * id gesetzt + externalId gesetzt -> "Löschen".
 */
public record RecipeDetailDto(
        Long id,
        String externalId,
        String title,
        String category,
        String region,
        String instructions,
        String imageUrl,
        List<IngredientDto> ingredients,
        boolean favorite
) {
}
