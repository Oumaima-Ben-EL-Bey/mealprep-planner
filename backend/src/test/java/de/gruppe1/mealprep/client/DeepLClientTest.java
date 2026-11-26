package de.gruppe1.mealprep.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class DeepLClientTest {

    @Autowired
    private DeepLClient client;

    @Test
    void translatesEnglishToGerman() {
        List<String> result = client.translate(List.of("Preheat the oven"), "EN", "DE");

        assertEquals(1, result.size());
        assertNotEquals("Preheat the oven", result.get(0));
    }
}