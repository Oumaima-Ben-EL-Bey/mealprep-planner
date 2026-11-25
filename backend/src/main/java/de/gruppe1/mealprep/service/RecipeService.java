package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.dto.RecipeDetailDto;
import de.gruppe1.mealprep.dto.RecipeRequestDto;
import de.gruppe1.mealprep.dto.RecipeSummaryDto;
import de.gruppe1.mealprep.entity.Recipe;
import de.gruppe1.mealprep.entity.RecipeIngredient;
import de.gruppe1.mealprep.exception.NotFoundException;
import de.gruppe1.mealprep.mapper.RecipeMapper;
import de.gruppe1.mealprep.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import de.gruppe1.mealprep.exception.ConflictException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    public List<RecipeSummaryDto> getAllRecipes(){

        List<Recipe> recipes = recipeRepository.findAll();
        List<RecipeSummaryDto> result = new ArrayList<>();

        for(
             Recipe recipe : recipes) {

            result.add(recipeMapper.toSummaryDto(recipe));


        }
        return result;

    }

    public RecipeDetailDto getRecipeById(Long id){


        return recipeMapper.toDetailDto(recipeRepository
                        .findById(id).orElseThrow(() -> new NotFoundException("Rezept nicht gefunden")));



    }

    public RecipeDetailDto create(RecipeRequestDto dto) {

        // dto umwandeln zu einer entity
        Recipe recipe = recipeMapper.toEntity(dto);

        // 2. recipe speichern
        Recipe savedRecipe = recipeRepository.save(recipe);

        // 3. entity wieder zu dto
        return recipeMapper.toDetailDto(savedRecipe);
    }
    public RecipeDetailDto update(Long id, RecipeRequestDto dto) {

        // Bestehendes Rezept finden
        Recipe recipe = recipeRepository
                .findById(id).orElseThrow(() -> new NotFoundException("Rezept nicht gefunden"));

        // Externe Rezepte dürfen nicht bearbeitet werden
        if (recipe.getExternalId() != null) {
            throw new ConflictException("Externe Rezepte können nicht bearbeitet werden");
        }

        // Alte Zutaten entfernen
        recipe.getIngredients().clear();

        // Neue Daten übernehmen
        Recipe updatedRecipe = recipeMapper.toEntity(dto);

        recipe.setTitle(updatedRecipe.getTitle());
        recipe.setCategory(updatedRecipe.getCategory());
        recipe.setRegion(updatedRecipe.getRegion());
        recipe.setInstructions(updatedRecipe.getInstructions());
        recipe.setImageUrl(updatedRecipe.getImageUrl());

        // Neue Zutaten hinzufügen
        for (RecipeIngredient ingredient : updatedRecipe.getIngredients()) {
            recipe.addIngredient(ingredient);
        }

        // Speichern und DTO zurückgeben
        Recipe savedRecipe = recipeRepository.save(recipe);

        return recipeMapper.toDetailDto(savedRecipe);
    }


    public void delete(Long id) {

        // Prüfen, ob Rezept existiert
        Recipe recipe = recipeRepository
                .findById(id).orElseThrow(() -> new NotFoundException("Rezept nicht gefunden"));

        // Rezept löschen
        recipeRepository.delete(recipe);
    }

}
