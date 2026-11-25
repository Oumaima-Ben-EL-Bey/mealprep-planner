
package de.gruppe1.mealprep.mapper;

import de.gruppe1.mealprep.dto.IngredientDto;
import de.gruppe1.mealprep.dto.RecipeDetailDto;
import de.gruppe1.mealprep.dto.RecipeRequestDto;
import de.gruppe1.mealprep.dto.RecipeSummaryDto;
import de.gruppe1.mealprep.entity.Recipe;
import de.gruppe1.mealprep.entity.RecipeIngredient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RecipeMapper {




    //umwandlung von dto zur Entity
    public Recipe toEntity(RecipeRequestDto dto) {

        Recipe recipe = new Recipe();

        recipe.setTitle(dto.title());
        recipe.setCategory(dto.category());
        recipe.setRegion(dto.region());
        recipe.setInstructions(dto.instructions());
        recipe.setImageUrl(dto.imageUrl());


        for (IngredientDto ingredient : dto.ingredients()) {

            RecipeIngredient recipeIngredient = new RecipeIngredient();

            recipeIngredient.setName(ingredient.name());
            recipeIngredient.setMeasure(ingredient.measure());



            recipe.addIngredient(recipeIngredient);
        }


        return recipe;
    }

    //umwandlung von entity zum -> dto
    public RecipeDetailDto toDetailDto(Recipe recipe) {
        List<IngredientDto> ingredients = new ArrayList<>();

        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            IngredientDto ingredientDto = new IngredientDto(
                    ingredient.getName(),
                    ingredient.getMeasure()
            );

            ingredients.add(ingredientDto);
        }

        return new RecipeDetailDto(
                recipe.getId(),
                recipe.getExternalId(),
                recipe.getTitle(),
                recipe.getCategory(),
                recipe.getRegion(),
                recipe.getInstructions(),
                recipe.getImageUrl(),
                ingredients,
                recipe.isFavorite()
        );
    }

    public RecipeSummaryDto toSummaryDto(Recipe recipe) {
        return new RecipeSummaryDto(
                recipe.getId(),
                recipe.getExternalId(),
                recipe.getTitle(),
                recipe.getImageUrl(),
                recipe.getCategory(),
                recipe.getRegion(),
                recipe.isFavorite()
        );
    }

}
