package de.gruppe1.mealprep.dto;

import jakarta.validation.constraints.NotNull;

public record CheckItemRequestDto(
        @NotNull(message = "Der Status darf nicht leer sein")
        Boolean checked
) {
}