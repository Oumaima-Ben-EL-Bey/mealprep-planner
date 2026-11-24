package de.gruppe1.mealprep.exception;

/** Wird zu 409: zum Beispiel ein Rezept, das schon in der Sammlung liegt. */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
