package de.gruppe1.mealprep.service;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MeasureCombinerTest {

    @Test
    void sumsSameUnit() {
        assertEquals("500 g", MeasureCombiner.combine(List.of("200 g", "300 g")));
    }

    @Test
    void sumsWithAndWithoutSpace() {
        assertEquals("150 g", MeasureCombiner.combine(List.of("100g", "50 g")));
    }

    @Test
    void treatsUnitSpellingsAsTheSameUnit() {
        assertEquals("3 EL", MeasureCombiner.combine(List.of("1 tbs", "2 tablespoons")));
    }

    @Test
    void sumsFractionsAndMixedNumbers() {
        assertEquals("2 Tassen", MeasureCombiner.combine(List.of("1/2 cup", "1 1/2 cups")));
    }

    @Test
    void sumsUnicodeFractionsWithGermanComma() {
        assertEquals("1,5 TL", MeasureCombiner.combine(List.of("½ tsp", "1 tsp")));
    }

    @Test
    void keepsDifferentUnitsSideBySide() {
        assertEquals("1 Tasse + 200 g", MeasureCombiner.combine(List.of("1 cup", "200 g")));
    }

    @Test
    void keepsTextOnceIgnoringCase() {
        assertEquals("2 + to taste", MeasureCombiner.combine(List.of("to taste", "To taste", "2")));
    }

    @Test
    void keepsRangesAsText() {
        assertEquals("1 + 2-3", MeasureCombiner.combine(List.of("2-3", "1")));
    }

    @Test
    void ignoresEmptyAndMissingMeasures() {
        assertEquals("", MeasureCombiner.combine(Arrays.asList("", null, "  ")));
    }
}