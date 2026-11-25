package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.controller.RecipeController;
import de.gruppe1.mealprep.entity.Recipe;
import de.gruppe1.mealprep.exception.ConflictException;
import de.gruppe1.mealprep.exception.GlobalExceptionHandler;
import de.gruppe1.mealprep.exception.NotFoundException;
import de.gruppe1.mealprep.repository.RecipeRepository;
import de.gruppe1.mealprep.mapper.RecipeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void externesRezeptKannNichtBearbeitetWerden() {

        // Arrange: externes Rezept vorbereiten
        Recipe recipe = new Recipe();
        recipe.setExternalId("52772");

        when(recipeRepository.findById(1L))
                .thenReturn(Optional.of(recipe));

        // Act + Assert: Update muss ConflictException werfen
        assertThrows(
                ConflictException.class,
                () -> recipeService.update(1L, null)
        );
    }

    @Test
    void unbekannteIdWirftNotFoundException() {

        when(recipeRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> recipeService.getRecipeById(999L)
        );
    }

    @Test
    void leererTitelErgibt400MitFieldErrors() throws Exception {

        RecipeService recipeService = org.mockito.Mockito.mock(RecipeService.class);
        RecipeMapper recipeMapper = org.mockito.Mockito.mock(RecipeMapper.class);

        RecipeController controller = new RecipeController(recipeService, recipeMapper);

        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        String json = """
            {
                "title": "",
                "category": "Lunch",
                "region": "Asia",
                "instructions": "Kochen",
                "imageUrl": null,
                "ingredients": [
                    {
                        "name": "Reis",
                        "measure": "100 g"
                    }
                ]
            }
            """;

        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/api/recipes")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isBadRequest())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.fieldErrors.title").exists());
    }
}