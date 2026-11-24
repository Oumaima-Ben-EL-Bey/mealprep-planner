package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.repository.RecipeRepository;
import org.springframework.stereotype.Service;


@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
}
