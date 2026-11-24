package de.gruppe1.mealprep.exception;

<<<<<<< HEAD
/** Wird zu 5xx: TheMealDB antwortet nicht, zu langsam oder mit Fehler. */
public class ExternalApiException extends RuntimeException {

    public ExternalApiException(String message) {
        super(message);
    }

    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
=======
public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
>>>>>>> origin/develop
