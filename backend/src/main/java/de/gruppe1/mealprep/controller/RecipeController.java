package de.gruppe1.mealprep.controller;

import de.gruppe1.mealprep.dto.FavoriteRequestDto;
import de.gruppe1.mealprep.dto.RecipeDetailDto;
import de.gruppe1.mealprep.dto.RecipeRequestDto;
import de.gruppe1.mealprep.dto.RecipeSummaryDto;
import de.gruppe1.mealprep.entity.Recipe;
import de.gruppe1.mealprep.mapper.RecipeMapper;
import de.gruppe1.mealprep.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeMapper recipeMapper;

    public RecipeController(RecipeService recipeService, RecipeMapper recipeMapper) {
        this.recipeService = recipeService;
        this.recipeMapper = recipeMapper;
    }

    @GetMapping("/api/recipes")
    public List<RecipeSummaryDto> getAllRecipes() {

        return  recipeService.getAllRecipes();
    }

    @GetMapping("/api/recipes/{id}")
    public RecipeDetailDto getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);

    }

    @PatchMapping("/api/recipes/{id}/favorite")
    public RecipeSummaryDto updateFavorite(@PathVariable Long id, @Valid @RequestBody FavoriteRequestDto dto) {
        return recipeService.setFavorite(id, dto);

    }




    @PostMapping("/api/recipes/from-external/{mealDbId}")
    public ResponseEntity<RecipeDetailDto> saveExternalRecipe(
            @PathVariable String mealDbId
    ) {
        RecipeDetailDto savedRecipe = recipeService.saveExternalRecipe(mealDbId);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

    @PostMapping("/api/recipes")
    public ResponseEntity<RecipeDetailDto> createRecipe(
            @Valid @RequestBody RecipeRequestDto dto) {

        RecipeDetailDto createdRecipe = recipeService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    @PutMapping("/api/recipes/{id}")
    public RecipeDetailDto updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody RecipeRequestDto dto) {

        return recipeService.update(id, dto);
    }

    @DeleteMapping("/api/recipes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {

        recipeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}