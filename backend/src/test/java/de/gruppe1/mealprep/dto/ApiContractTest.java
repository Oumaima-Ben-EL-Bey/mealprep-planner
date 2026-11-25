package de.gruppe1.mealprep.dto;

import de.gruppe1.mealprep.exception.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prüft den in F4 festgelegten API-Vertrag: die DTO-Records haben die Felder,
 * auf die sich alle Slices geeinigt haben, und der Handler liefert die
 * passenden Statuscodes mit der einen Fehlerform.
 */
class ApiContractTest {

    @Test
    void summaryDtoHatDieVertragsfelder() {
        RecipeSummaryDto dto = new RecipeSummaryDto(
                7L, "52772", "Titel", "https://bild", "Dessert", "Argentina", true);

        assertNull(new RecipeSummaryDto(null, "52772", "t", "i", "c", "r", false).id(),
                "Suchtreffer haben keine eigene id");
        assertEquals(7L, dto.id());
        assertEquals("52772", dto.externalId());
        assertTrue(dto.favorite());
    }

    @Test
    void detailDtoTraegtZutatenliste() {
        RecipeDetailDto dto = new RecipeDetailDto(
                8L, null, "Couscous", "Seafood", "Tunisia", "Kochen", null,
                List.of(new IngredientDto("Couscous", "500 g")), false);

        assertEquals(1, dto.ingredients().size());
        assertEquals("500 g", dto.ingredients().get(0).measure());
        assertNull(dto.externalId(), "eigene Rezepte haben keine externalId");
    }

    @Test
    void fehlerformHatMessageTimestampUndFeldFehler() {
        ErrorResponseDto fehler = new ErrorResponseDto("Eingaben ungültig", null,
                Map.of("title", "Titel darf nicht leer sein"));

        assertNotNull(fehler.message());
        assertEquals("Titel darf nicht leer sein", fehler.fieldErrors().get("title"));
        assertNull(new ErrorResponseDto("nicht gefunden", java.time.LocalDateTime.now(), null).fieldErrors());
    }

    @RestController
    static class Werfer {
        @GetMapping("/test/notfound") void nf() { throw new NotFoundException("Rezept nicht gefunden"); }
        @GetMapping("/test/conflict") void cf() { throw new ConflictException("Schon in deiner Sammlung"); }
        @GetMapping("/test/external") void ex() { throw new ExternalApiException("TheMealDB antwortet nicht"); }
    }

    private MockMvc mvc() {
        return MockMvcBuilders.standaloneSetup(new Werfer())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void notFoundWird404() throws Exception {
        mvc().perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/test/notfound"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isNotFound())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$.message").value("Rezept nicht gefunden"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$.timestamp").exists());
    }

    @Test
    void conflictWird409() throws Exception {
        mvc().perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/test/conflict"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void fehlendeExterneApiWird5xx() throws Exception {
        int status = mvc().perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/test/external"))
                .andReturn().getResponse().getStatus();
        assertTrue(status == 502 || (status >= 500 && status <= 599),
                "erwartet 5xx, war " + status);
    }
}
