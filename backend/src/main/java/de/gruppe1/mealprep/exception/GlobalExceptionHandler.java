package de.gruppe1.mealprep.exception;

import de.gruppe1.mealprep.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Skeleton aus F4: not found, conflict und eine fehlende externe API.
 * B1 ergänzt hier die Validierung (MethodArgumentNotValidException -> 400 mit fieldErrors).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

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


    private ResponseEntity<ErrorResponseDto> antwort(HttpStatus status, String message,
                                                     java.util.Map<String, String> fieldErrors) {
        return ResponseEntity.status(status)
                .body(new ErrorResponseDto(message, LocalDateTime.now(), fieldErrors));
    }
}
