package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.dto.ManualItemRequestDto;
import de.gruppe1.mealprep.dto.ShoppingListItemDto;
import de.gruppe1.mealprep.entity.*;
import de.gruppe1.mealprep.exception.NotFoundException;
import de.gruppe1.mealprep.repository.MealPlanEntryRepository;
import de.gruppe1.mealprep.repository.PantryItemRepository;
import de.gruppe1.mealprep.repository.RecipeRepository;
import de.gruppe1.mealprep.repository.ShoppingListItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ShoppingListServiceTest {

    @Autowired
    private ShoppingListService service;
    @Autowired
    private ShoppingListItemRepository itemRepository;
    @Autowired
    private MealPlanEntryRepository planRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private PantryItemRepository pantryRepository;

    @BeforeEach
    void clean() {
        planRepository.deleteAllInBatch();
        itemRepository.deleteAllInBatch();
        pantryRepository.deleteAllInBatch();
    }

    private Recipe recipe(String title, String... nameAndMeasure) {
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setCategory("Pasta");
        recipe.setInstructions("Kochen.");
        for (int i = 0; i < nameAndMeasure.length; i += 2) {
            RecipeIngredient ingredient = new RecipeIngredient();
            ingredient.setName(nameAndMeasure[i]);
            ingredient.setMeasure(nameAndMeasure[i + 1]);
            recipe.addIngredient(ingredient);
        }
        return recipeRepository.save(recipe);
    }

    private void plan(Recipe recipe, DayOfWeek day, MealType mealType) {
        MealPlanEntry entry = new MealPlanEntry();
        entry.setRecipe(recipe);
        entry.setDayOfWeek(day);
        entry.setMealType(mealType);
        planRepository.save(entry);
    }

    private ShoppingListItemDto find(List<ShoppingListItemDto> items, String name) {
        return items.stream().filter(i -> i.name().equalsIgnoreCase(name)).findFirst().orElseThrow();
    }

    @Test
    void generate_GroupsIgnoringCaseAndCountsDoubleRecipesTwice() {
        Recipe a = recipe("A", "Tomaten", "200 g", "Knoblauch", "1 Zehe");
        Recipe b = recipe("B", "tomaten ", "1 Dose");
        plan(a, DayOfWeek.MONDAY, MealType.LUNCH);
        plan(a, DayOfWeek.TUESDAY, MealType.DINNER);
        plan(b, DayOfWeek.MONDAY, MealType.DINNER);

        List<ShoppingListItemDto> items = service.generateFromMealPlan();

        assertEquals(2, items.size());
        assertEquals("400 g + 1 Dose", find(items, "Tomaten").measure());
        assertEquals("2 Zehe", find(items, "Knoblauch").measure());
    }

    @Test
    void generate_KeepsManualItemsAndReplacesGeneratedOnes() {
        plan(recipe("A", "Reis", "500 g"), DayOfWeek.MONDAY, MealType.LUNCH);
        service.addManualItem(new ManualItemRequestDto("Kaffee", ""));

        service.generateFromMealPlan();
        List<ShoppingListItemDto> items = service.generateFromMealPlan();

        assertEquals(2, items.size());
        assertTrue(find(items, "Kaffee").manual());
        assertFalse(find(items, "Reis").manual());
    }

    @Test
    void setChecked_CanCheckAndUncheck() {
        Long id = service.addManualItem(new ManualItemRequestDto("Milch", "1 l")).id();

        assertTrue(service.setChecked(id, true).checked());
        assertFalse(service.setChecked(id, false).checked());
    }

    @Test
    void setChecked_UnknownIdThrowsNotFound() {
        assertThrows(NotFoundException.class, () -> service.setChecked(-1L, true));
    }

    @Test
    void deleteCheckedItems_RemovesOnlyCheckedItems() {
        Long milk = service.addManualItem(new ManualItemRequestDto("Milch", "1 l")).id();
        service.addManualItem(new ManualItemRequestDto("Brot", ""));
        service.setChecked(milk, true);

        service.deleteCheckedItems();

        List<ShoppingListItemDto> items = service.getList();
        assertEquals(1, items.size());
        assertEquals("Brot", items.get(0).name());
    }

    @Test
    void generate_MarksItemsAlreadyInPantry() {
        PantryItem rice = new PantryItem();
        rice.setName("Rice");
        pantryRepository.save(rice);
        plan(recipe("A", "rice ", "500 g", "Butter", "50 g"), DayOfWeek.MONDAY, MealType.LUNCH);
        service.addManualItem(new ManualItemRequestDto("Rice", ""));

        List<ShoppingListItemDto> items = service.generateFromMealPlan();

        assertTrue(items.stream().anyMatch(i -> !i.manual() && i.name().equals("rice") && i.inPantry()));
        assertFalse(find(items, "Butter").inPantry());
        assertTrue(items.stream().anyMatch(i -> i.manual() && !i.inPantry()));
    }
}