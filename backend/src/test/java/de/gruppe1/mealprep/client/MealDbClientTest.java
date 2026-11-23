package de.gruppe1.mealprep.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest()
public class MealDbClientTest {
    @Autowired
    private MealDbClient client;

    @Test
    void searchByName_ListIsNotEmpty() {

        List<MealDbMeal> meals = client.searchByName("Arrabiata");

        assertFalse(meals.isEmpty());

    }

    @Test
    void searchByName_ListIsEmpty() {
        List<MealDbMeal> meals = client.searchByName("NotAMeal");

        assertTrue(meals.isEmpty());
    }
}