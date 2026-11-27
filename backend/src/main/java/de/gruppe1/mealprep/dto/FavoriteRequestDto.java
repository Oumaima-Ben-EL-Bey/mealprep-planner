package de.gruppe1.mealprep.dto;

import jakarta.validation.constraints.NotNull;

public record FavoriteRequestDto(
        @NotNull(message = "Der Favoritenstatus fehlt")
        Boolean favorite
) {
}