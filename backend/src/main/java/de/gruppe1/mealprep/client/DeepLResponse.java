package de.gruppe1.mealprep.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeepLResponse(List<DeepLTranslation> translations) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DeepLTranslation(String text) {
    }
}