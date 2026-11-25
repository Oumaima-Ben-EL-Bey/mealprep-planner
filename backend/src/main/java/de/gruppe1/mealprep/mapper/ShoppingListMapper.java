package de.gruppe1.mealprep.mapper;

import de.gruppe1.mealprep.dto.ShoppingListItemDto;
import de.gruppe1.mealprep.entity.ShoppingListItem;
import org.springframework.stereotype.Component;

@Component
public class ShoppingListMapper {

    public ShoppingListItemDto toDto(ShoppingListItem item) {
        return new ShoppingListItemDto(
                item.getId(),
                item.getName(),
                item.getMeasure(),
                item.isChecked(),
                item.isManual(),
                item.isInPantry()
        );
    }
}