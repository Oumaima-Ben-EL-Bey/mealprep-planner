package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.entity.Recipe;
import de.gruppe1.mealprep.exception.NotFoundException;
import de.gruppe1.mealprep.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes(){
       return recipeRepository.findAll();
    }

    public Recipe getRecipeById(Long id){
      return recipeRepository.findById(id)
              .orElseThrow(() -> new NotFoundException("Recipe not found"));
    }

}
