package event.website.config;

import event.website.config.TrainingSavingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
public class CustomExceptionHandler {

    // Standardized method for creating error response
    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String type, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error_code", status.value());
        response.put("type", type);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyDeletedException(AlreadyDeletedException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "already_deleted", e.getMessage());
    }
    // Handle MethodArgumentNotValidException (validation errors)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            message.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        return createErrorResponse(HttpStatus.BAD_REQUEST, "validation", message.toString().trim());
    }

    // Handle ConstraintViolationException (constraint violations)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder violationMessages = new StringBuilder();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            violationMessages.append(violation.getPropertyPath()).append(": ")
                    .append(violation.getMessage()).append("; ");
        }
        return createErrorResponse(HttpStatus.BAD_REQUEST, "validation", violationMessages.toString().trim());
    }

    // Handle UsernameNotFoundException (user not found errors)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "user", e.getMessage());
    }

    // Handle UserSavingException (user saving related errors)
    @ExceptionHandler(UserSavingException.class)
    public ResponseEntity<Map<String, Object>> handleUserSavingException(UserSavingException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "user validation", e.getMessage());
    }

    // Handle SQLIntegrityConstraintViolationException (specific SQL error cases, like unique constraint violations)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        String message = "Database constraint violation: " + e.getMessage();
        return createErrorResponse(HttpStatus.BAD_REQUEST, "constraint", message);
    }

    // Handle runtime exceptions like NullPointerException, IllegalArgumentException, etc.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "runtime", e.getMessage());
    }

    // Handle DataIntegrityViolationException (specific database constraint violations like unique key violations)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = "Database integrity constraint violation: " + e.getMessage();
        return createErrorResponse(HttpStatus.CONFLICT, "constraint", message);
    }



    // Handle general exceptions (catch-all for unexpected errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception e) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "general", e.getMessage());
    }


    @ExceptionHandler(TrainingSavingException.class)
    public ResponseEntity<Map<String, Object>> handleTrainingSavingException(TrainingSavingException e) {
        String message = e.getMessage();
        String type = message.contains("Training capacity") ? "training_capacity" : "training_validation";
        return createErrorResponse(HttpStatus.BAD_REQUEST, type, message);
    }


}
