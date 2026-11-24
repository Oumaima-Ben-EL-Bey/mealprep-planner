package de.gruppe1.mealprep.exception;

/** Wird zu 5xx: TheMealDB antwortet nicht, zu langsam oder mit Fehler. */
public class ExternalApiException extends RuntimeException {

    public ExternalApiException(String message) {
        super(message);
    }

    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
