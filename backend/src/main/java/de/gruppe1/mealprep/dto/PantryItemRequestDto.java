package de.gruppe1.mealprep.dto;

import jakarta.validation.constraints.NotBlank;

public record PantryItemRequestDto(@NotBlank(message = "Name darf nicht leer sein") String name, String measure) {
}
