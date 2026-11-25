package de.gruppe1.mealprep.mapper;

import de.gruppe1.mealprep.dto.MealPlanEntryDto;
import de.gruppe1.mealprep.entity.MealPlanEntry;
import org.springframework.stereotype.Component;

@Component
public class MealPlanMapper {
    private final RecipeMapper recipeMapper;
    public MealPlanMapper(RecipeMapper recipeMapper) {
        this.recipeMapper = recipeMapper;
    }

    public MealPlanEntryDto toDto(MealPlanEntry entry) {
        return new MealPlanEntryDto(
                entry.getId(),
                entry.getDayOfWeek(),
                entry.getMealType(),
                recipeMapper.toSummaryDto(entry.getRecipe())
        );
    }
}
