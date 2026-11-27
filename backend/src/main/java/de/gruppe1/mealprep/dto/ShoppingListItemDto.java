package de.gruppe1.mealprep.dto;

public record ShoppingListItemDto(
        Long id,
        String name,
        String displayName,
        String measure,
        boolean checked,
        boolean manual,
        boolean inPantry) {
}