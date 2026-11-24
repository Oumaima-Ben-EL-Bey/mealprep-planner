package de.gruppe1.mealprep.dto;

import jakarta.validation.constraints.NotNull;

public record AssignRecipeRequestDto(
        @NotNull(message = "Rezept-ID darf nicht leer sein")
        Long recipeId
) {
}
