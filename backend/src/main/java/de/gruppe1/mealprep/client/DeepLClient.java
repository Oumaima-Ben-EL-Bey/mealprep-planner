package de.gruppe1.mealprep.client;

import de.gruppe1.mealprep.exception.ExternalApiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class DeepLClient {

    private final RestClient restClient;

    public DeepLClient(@Qualifier("deepLRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<String> translate(List<String> texts, String sourceLang, String targetLang) {
        DeepLResponse response;
        try {
            response = restClient.post()
                    .uri("/v2/translate")
                    .body(new DeepLRequest(texts, sourceLang, targetLang))
                    .retrieve()
                    .body(DeepLResponse.class);
        } catch (RestClientException e) {
            throw new ExternalApiException("DeepL ist nicht erreichbar", e);
        }
        if (response == null || response.translations() == null) {
            throw new ExternalApiException("DeepL hat keine Übersetzung geliefert", null);
        }
        return response.translations().stream().map(DeepLResponse.DeepLTranslation::text).toList();
    }
}