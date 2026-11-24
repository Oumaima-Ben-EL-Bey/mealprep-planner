package de.gruppe1.mealprep.dto;

/** Eine Zutatenzeile eines Rezepts, measure ist ein freier Text wie "500 g". */
public record IngredientDto(
        String name,
        String measure
) {
}
