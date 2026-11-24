package de.gruppe1.mealprep.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Eine Fehlerform für die ganze App.
 * fieldErrors ist bei Validierungsfehlern gefüllt (Feld -> Meldung),
 * sonst null.
 */
public record ErrorResponseDto(
        String message,
        LocalDateTime timestamp,
        Map<String, String> fieldErrors
) {
}
