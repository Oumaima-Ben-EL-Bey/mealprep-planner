package de.gruppe1.mealprep.dto;

import jakarta.validation.constraints.NotBlank;

public record ManualItemRequestDto(
        @NotBlank(message = "Der Artikelname darf nicht leer sein")
        String name,
        String measure
) {
}