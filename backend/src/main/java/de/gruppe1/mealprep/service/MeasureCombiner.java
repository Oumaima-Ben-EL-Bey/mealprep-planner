package de.gruppe1.mealprep.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fasst Mengenangaben zusammen: gleiche Einheiten werden addiert,
 * alles andere bleibt als Text nebeneinander stehen.
 */
public final class MeasureCombiner {

    // Zahl am Anfang: "1 1/2", "1/2", "1½", "½", "1.5", "1,5", "200"
    private static final Pattern NUMBER_AND_UNIT = Pattern.compile(
            "^(\\d+\\s+\\d+/\\d+|\\d+/\\d+|\\d+\\s*[½¼¾⅓⅔]|[½¼¾⅓⅔]|\\d+(?:[.,]\\d+)?)\\s*(.*)$");

    private static final Map<String, String> UNIT_ALIASES = Map.ofEntries(
            Map.entry("gram", "g"), Map.entry("grams", "g"), Map.entry("gr", "g"),
            Map.entry("kilogram", "kg"), Map.entry("kilograms", "kg"),
            Map.entry("millilitre", "ml"), Map.entry("millilitres", "ml"),
            Map.entry("milliliter", "ml"), Map.entry("milliliters", "ml"),
            Map.entry("litre", "l"), Map.entry("litres", "l"), Map.entry("liter", "l"), Map.entry("liters", "l"),
            Map.entry("tbs", "tbsp"), Map.entry("tblsp", "tbsp"), Map.entry("tbls", "tbsp"),
            Map.entry("tablespoon", "tbsp"), Map.entry("tablespoons", "tbsp"),
            Map.entry("teaspoon", "tsp"), Map.entry("teaspoons", "tsp"), Map.entry("tspn", "tsp"),
            Map.entry("cups", "cup"),
            Map.entry("ounce", "oz"), Map.entry("ounces", "oz"),
            Map.entry("lbs", "lb"), Map.entry("pound", "lb"), Map.entry("pounds", "lb"),
            Map.entry("cloves", "clove")
            , Map.entry("pinches", "pinch")
    );
    // Englische Küchen-Einheiten auf Deutsch
    private static final Map<String, String> GERMAN_UNITS = Map.of(
            "tbsp", "EL", "tsp", "TL", "clove", "Zehe", "cup", "Tasse", "pinch", "Prise");

    private MeasureCombiner() {
    }

    public static String combine(List<String> measures) {
        Map<String, Double> sums = new LinkedHashMap<>();
        Map<String, String> unitLabels = new LinkedHashMap<>();
        Map<String, String> texts = new LinkedHashMap<>();

        for (String measure : measures) {
            if (measure == null || measure.isBlank()) {
                continue;
            }
            String trimmed = measure.trim();
            Matcher matcher = NUMBER_AND_UNIT.matcher(trimmed);
            String unit = matcher.matches() ? matcher.group(2).trim() : null;

            if (unit == null || startsLikeANumber(unit)) {
                texts.putIfAbsent(trimmed.toLowerCase(Locale.ROOT), trimmed);
                continue;
            }
            String key = normalizeUnit(unit);
            sums.merge(key, parseNumber(matcher.group(1)), Double::sum);
            unitLabels.putIfAbsent(key, unit);
        }


        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Double> sum : sums.entrySet()) {
            String label = germanUnit(sum.getKey(), unitLabels.get(sum.getKey()), sum.getValue());            append(result, formatNumber(sum.getValue()) + (label.isEmpty() ? "" : " " + label));
        }
        for (String text : texts.values()) {
            append(result, text);
        }
        return result.toString();
    }
    // Zehe, Tasse und Prise bekommen ab mehr als 1 ein "n": 2 Zehen, 3 Tassen
    private static String germanUnit(String key, String label, double value) {
        String german = GERMAN_UNITS.get(key);
        if (german == null) {
            return label;
        }
        if (value > 1 && german.endsWith("e")) {
            return german + "n";
        }
        return german;
    }
    private static boolean startsLikeANumber(String unit) {
        return !unit.isEmpty() && (Character.isDigit(unit.charAt(0)) || unit.startsWith("-") || unit.startsWith("/"));
    }

    private static String normalizeUnit(String unit) {
        String lower = unit.toLowerCase(Locale.ROOT);
        return UNIT_ALIASES.getOrDefault(lower, lower);
    }

    private static double parseNumber(String number) {
        String spaced = number
                .replace("½", " 1/2").replace("¼", " 1/4").replace("¾", " 3/4")
                .replace("⅓", " 1/3").replace("⅔", " 2/3")
                .trim();
        double total = 0;
        for (String part : spaced.split("\\s+")) {
            if (part.contains("/")) {
                String[] fraction = part.split("/");
                total += Double.parseDouble(fraction[0]) / Double.parseDouble(fraction[1]);
            } else {
                total += Double.parseDouble(part.replace(',', '.'));
            }
        }
        return total;
    }

    private static String formatNumber(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
                .replace('.', ',');
    }

    private static void append(StringBuilder result, String part) {
        if (!result.isEmpty()) {
            result.append(" + ");
        }
        result.append(part);
    }
}