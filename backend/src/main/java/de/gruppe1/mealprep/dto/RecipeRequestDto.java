
package de.gruppe1.mealprep.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record RecipeRequestDto(

        @NotBlank(message = "Der Titel darf nicht leer sein")
        String title,

        @NotBlank(message = "Die Kategorie darf nicht leer sein")
        String category,

        String region,

        @NotBlank(message = "Die Anleitung darf nicht leer sein")
        String instructions,

        String imageUrl,

        @NotEmpty(message = "Mindestens eine Zutat ist erforderlich")
        List<@Valid IngredientDto> ingredients

) {
}
