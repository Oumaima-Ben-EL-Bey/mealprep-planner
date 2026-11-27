package de.gruppe1.mealprep.exception;

import de.gruppe1.mealprep.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Skeleton aus F4: not found, conflict und eine fehlende externe API.
 * B1 ergänzt hier die Validierung (MethodArgumentNotValidException -> 400 mit fieldErrors).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(NotFoundException ex) {
        return antwort(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleConflict(ConflictException ex) {
        return antwort(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ErrorResponseDto> handleExternalApi(ExternalApiException ex) {
        return antwort(HttpStatus.BAD_GATEWAY, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        return antwort(HttpStatus.BAD_REQUEST, "Validierungsfehler", fieldErrors);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponseDto> handleBadRequest(Exception ex) {
        return antwort(HttpStatus.BAD_REQUEST, "Die Anfrage enthält ungültige Werte.", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnexpected(Exception ex) {
        if (ex instanceof ErrorResponse errorResponse) {
            HttpStatus status = HttpStatus.valueOf(errorResponse.getStatusCode().value());
            return antwort(status, "Die Anfrage konnte nicht verarbeitet werden.", null);
        }
        log.error("Unerwarteter Fehler", ex);
        return antwort(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ein unerwarteter Fehler ist aufgetreten. Bitte versuche es später noch einmal.", null);
    }

    private ResponseEntity<ErrorResponseDto> antwort(HttpStatus status, String message,
                                                     java.util.Map<String, String> fieldErrors) {
        return ResponseEntity.status(status)
                .body(new ErrorResponseDto(message, LocalDateTime.now(), fieldErrors));
    }
}