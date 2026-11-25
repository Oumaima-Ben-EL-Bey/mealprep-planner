package de.gruppe1.mealprep.dto;

import jakarta.validation.constraints.NotBlank;

/** Eine Zutatenzeile eines Rezepts, measure ist ein freier Text wie "500 g". */
public record IngredientDto(
        @NotBlank(message = "Name darf nicht leer sein")
        String name,
        @NotBlank(message = "Menge darf nicht leer sein")
        String measure
) {
}
