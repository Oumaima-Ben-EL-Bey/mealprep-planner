package de.gruppe1.mealprep.controller;

import de.gruppe1.mealprep.dto.RecipeSummaryDto;
import de.gruppe1.mealprep.service.ExternalRecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/external")
public class ExternalRecipeController {
    private final ExternalRecipeService externalRecipeService;

    public ExternalRecipeController(ExternalRecipeService externalRecipeService) {
        this.externalRecipeService = externalRecipeService;
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeSummaryDto>> getExternalRecipes(@RequestParam(name = "search", defaultValue = "") String searchTerm, @RequestParam(defaultValue = "") String category) {
        List<RecipeSummaryDto> recipes = externalRecipeService.search(searchTerm, category);
        return ResponseEntity.ok(recipes);
    }
}