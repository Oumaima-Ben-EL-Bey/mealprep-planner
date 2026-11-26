package de.gruppe1.mealprep.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DeepLRequest(
        List<String> text,
        @JsonProperty("source_lang") String sourceLang,
        @JsonProperty("target_lang") String targetLang,
        String formality

) {
}