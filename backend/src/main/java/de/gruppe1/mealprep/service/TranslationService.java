package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.client.DeepLClient;
import de.gruppe1.mealprep.entity.Translation;
import de.gruppe1.mealprep.repository.TranslationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TranslationService {

    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);
    private static final int BATCH_SIZE = 50;

    private final DeepLClient deepLClient;
    private final TranslationRepository repository;
    private final boolean enabled;

    public TranslationService(DeepLClient deepLClient, TranslationRepository repository,
                              @Value("${deepl.api-key}") String apiKey) {
        this.deepLClient = deepLClient;
        this.repository = repository;
        this.enabled = !apiKey.isBlank();
    }

    public List<String> toGerman(List<String> texts) {
        return translate(texts, "EN", "DE");
    }

    public String toEnglish(String text) {
        return translate(List.of(text), "DE", "EN-US").get(0);
    }

    private List<String> translate(List<String> texts, String sourceLang, String targetLang) {
        List<String> result = new ArrayList<>(texts);
        List<String> hashes = texts.stream().map(t -> hash(targetLang, t)).toList();

        Map<String, String> cached = repository.findByHashIn(hashes).stream()
                .collect(Collectors.toMap(Translation::getHash, Translation::getTranslatedText, (a, b) -> a));

        List<Integer> missing = new ArrayList<>();
        for (int i = 0; i < texts.size(); i++) {
            String text = texts.get(i);
            if (text == null || text.isBlank()) {
                continue;
            }
            String hit = cached.get(hashes.get(i));
            if (hit != null) {
                result.set(i, hit);
            } else {
                missing.add(i);
            }
        }

        if (missing.isEmpty() || !enabled) {
            return result;
        }

        try {
            for (int start = 0; start < missing.size(); start += BATCH_SIZE) {
                List<Integer> batch = missing.subList(start, Math.min(start + BATCH_SIZE, missing.size()));
                List<String> translated = deepLClient.translate(
                        batch.stream().map(texts::get).toList(), sourceLang, targetLang);
                for (int j = 0; j < batch.size(); j++) {
                    int index = batch.get(j);
                    result.set(index, translated.get(j));
                    save(hashes.get(index), translated.get(j));
                }
            }
        } catch (RuntimeException e) {
            log.warn("Übersetzung fehlgeschlagen, Originaltext wird angezeigt", e);
        }
        return result;
    }

    private void save(String hash, String translatedText) {
        Translation translation = new Translation();
        translation.setHash(hash);
        translation.setTranslatedText(translatedText);
        repository.save(translation);
    }

    private static String hash(String targetLang, String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest((targetLang + "|" + text).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}