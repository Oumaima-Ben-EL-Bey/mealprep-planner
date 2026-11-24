package de.gruppe1.mealprep.controller;

import de.gruppe1.mealprep.service.ExternalRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external")
public class ExternalRecipeController {
    @Autowired
    private ExternalRecipeService externalRecipeService;

}