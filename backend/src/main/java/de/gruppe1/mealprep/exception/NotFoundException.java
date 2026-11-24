package de.gruppe1.mealprep.exception;

/** Wird zu 404: das Rezept gibt es nicht. */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
