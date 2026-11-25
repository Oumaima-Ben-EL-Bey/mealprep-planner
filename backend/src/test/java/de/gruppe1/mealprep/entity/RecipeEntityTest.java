package de.gruppe1.mealprep.entity;

import de.gruppe1.mealprep.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Echter Durchlauf gegen Postgres: speichern, laden, kaskadierend löschen.
 * @Transactional rollt am Ende alles zurück, die Entwicklungsdatenbank bleibt sauber.
 */
@SpringBootTest
@Transactional
class RecipeEntityTest {

    @Autowired private RecipeRepository recipes;
    @Autowired private JdbcTemplate jdbc;

    private Recipe rezept(String titel, String externalId) {
        Recipe r = new Recipe();
        r.setTitle(titel);
        r.setCategory("Seafood");
        r.setRegion("Tunisia");
        r.setInstructions("Alles zusammen kochen.");
        r.setExternalId(externalId);
        return r;
    }

    private RecipeIngredient zutat(String name, String measure) {
        RecipeIngredient z = new RecipeIngredient();
        z.setName(name);
        z.setMeasure(measure);
        return z;
    }

    @Test
    void speichertRezeptMitZutatenUndSetztDieFremdschluessel() {
        Recipe r = rezept("Couscous", "52772");
        r.addIngredient(zutat("Couscous", "500 g"));
        r.addIngredient(zutat("Fisch", "1 kg"));

        Recipe gespeichert = recipes.saveAndFlush(r);

        assertNotNull(gespeichert.getId());
        assertEquals(2, gespeichert.getIngredients().size());
        assertSame(gespeichert, gespeichert.getIngredients().get(0).getRecipe(),
                "addIngredient muss die Gegenrichtung setzen");

        Integer zeilen = jdbc.queryForObject(
                "select count(*) from recipe_ingredients where recipe_id = ?", Integer.class, gespeichert.getId());
        assertEquals(2, zeilen, "beide Zutaten müssen mit recipe_id in der Datenbank stehen");
    }

    @Test
    void findByIdLiefertDasRezeptMitZutaten() {
        Recipe r = rezept("Couscous", "52772");
        r.addIngredient(zutat("Couscous", "500 g"));
        Recipe gespeichert = recipes.saveAndFlush(r);

        Recipe geladen = recipes.findById(gespeichert.getId()).orElseThrow();

        assertEquals("Couscous", geladen.getTitle());
        assertEquals(1, geladen.getIngredients().size());
        assertEquals("500 g", geladen.getIngredients().get(0).getMeasure());
    }

    @Test
    void existsByExternalIdFindetGespeicherteRezepte() {
        recipes.saveAndFlush(rezept("Couscous", "52772"));

        assertTrue(recipes.existsByExternalId("52772"));
        assertFalse(recipes.existsByExternalId("99999"));
    }

    @Test
    void loeschenNimmtDieZutatenMit() {
        Recipe r = rezept("Couscous", "52772");
        r.addIngredient(zutat("Couscous", "500 g"));
        r.addIngredient(zutat("Fisch", "1 kg"));
        Recipe gespeichert = recipes.saveAndFlush(r);
        Long id = gespeichert.getId();

        recipes.deleteById(id);
        recipes.flush();

        assertEquals(0, (int) jdbc.queryForObject(
                "select count(*) from recipe_ingredients where recipe_id = ?", Integer.class, id),
                "cascade = ALL muss die Zutaten mitlöschen");
        assertTrue(recipes.findById(id).isEmpty());
    }

    @Test
    void eigenesRezeptHatKeineExternalId() {
        Recipe r = rezept("Mein Couscous", null);
        r.addIngredient(zutat("Couscous", "500 g"));

        Recipe gespeichert = recipes.saveAndFlush(r);

        assertNull(gespeichert.getExternalId());
        assertFalse(gespeichert.isFavorite(), "favorite startet auf false");
    }
}
