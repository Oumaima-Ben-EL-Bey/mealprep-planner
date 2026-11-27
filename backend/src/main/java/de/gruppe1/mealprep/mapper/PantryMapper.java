package de.gruppe1.mealprep.mapper;

import de.gruppe1.mealprep.dto.PantryItemDto;
import de.gruppe1.mealprep.entity.PantryItem;
import org.springframework.stereotype.Component;

@Component
public class PantryMapper {
    public PantryItemDto toDto(PantryItem item){
        return new PantryItemDto(item.getId(), item.getName(), item.getMeasure());
    }
}
