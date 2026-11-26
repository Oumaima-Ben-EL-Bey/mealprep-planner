package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.repository.TranslationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TranslationServiceTest {

    @Autowired private TranslationService service;
    @Autowired private TranslationRepository repository;

    @Test
    void translatesAndStoresInCache() {
        long before = repository.count();

        List<String> first = service.toGerman(List.of("Boil the water"));
        List<String> second = service.toGerman(List.of("Boil the water"));

        assertNotEquals("Boil the water", first.get(0));
        assertEquals(first, second);
        assertEquals(before + 1, repository.count());
    }

    @Test
    void keepsEmptyAndMissingTextsUnchanged() {
        List<String> result = service.toGerman(Arrays.asList("", null));

        assertEquals("", result.get(0));
        assertNull(result.get(1));
    }

    @Test
    void translatesSearchTermToEnglish() {
        assertEquals("chicken", service.toEnglish("Hähnchen").toLowerCase());
    }
}