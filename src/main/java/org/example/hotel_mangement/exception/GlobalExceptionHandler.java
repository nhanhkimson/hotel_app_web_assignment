package org.example.hotel_mangement.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Convert camelCase or PascalCase to SCREAMING_SNAKE_CASE
     * e.g., "ValidationError" -> "VALIDATION_ERROR"
     */
    private String toScreamingSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return "UNKNOWN_ERROR";
        }
        return input.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
    }

    private Map<String, Object> buildErrorBody(
            HttpStatus status, String type, String errorMessage, HttpServletRequest request) {

        Instant now = Instant.now();
        DateTimeFormatter readableFormatter = DateTimeFormatter
                .ofPattern("EEEE, dd MMMM yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code", status.value());
        error.put("type", toScreamingSnakeCase(type));
        error.put("errorMessage", errorMessage != null ? errorMessage : "An error occurred.");
        error.put("path", request != null ? request.getRequestURI() : null);
        error.put("timestamp", now.toString());
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("errorDate", readableFormatter.format(now));
        response.put("success", false);
        response.put("errors", error);
        return response;
    }

    // Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        // Get only the first validation error
        FieldError firstError = ex.getBindingResult().getFieldErrors().isEmpty() ? null 
                : ex.getBindingResult().getFieldErrors().get(0);
        
        String errorMessage = firstError != null 
                ? firstError.getDefaultMessage()
                : "Validation failed for one or more fields.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorBody(HttpStatus.BAD_REQUEST, "ValidationError",
                        errorMessage, request));
    }

    // IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorBody(HttpStatus.BAD_REQUEST, "BadRequest",
                        ex.getMessage() != null ? ex.getMessage() : "Invalid argument provided.", request));
    }

    // Generic bad request exception
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorBody(HttpStatus.BAD_REQUEST, "BadRequest",
                        ex.getMessage() != null ? ex.getMessage() : "Bad request.", request));
    }

    // Not found variants
    @ExceptionHandler({NotFoundException.class, ItemNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorBody(HttpStatus.NOT_FOUND, "NotFound",
                        ex.getMessage() != null ? ex.getMessage() : "Item not found.", request));
    }

    // User not found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorBody(HttpStatus.NOT_FOUND, "UserNotFound",
                        ex.getMessage() != null ? ex.getMessage() : "User not found.", request));
    }

    // Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildErrorBody(HttpStatus.UNAUTHORIZED, "Unauthorized",
                        ex.getMessage() != null ? ex.getMessage() : "Unauthorized access.", request));
    }

    // Conflict
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ConflictException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildErrorBody(HttpStatus.CONFLICT, "Conflict",
                        ex.getMessage() != null ? ex.getMessage() : "Conflict occurred.", request));
    }

    // Method parameter validation
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, Object>> handleMethodValidation(HandlerMethodValidationException ex, HttpServletRequest request) {
        // Get only the first validation error
        String errorMessage = "Method parameter validation failed.";
        
        var validationResults = ex.getParameterValidationResults();
        if (!validationResults.isEmpty()) {
            var firstParamError = validationResults.iterator().next();
            var resolvableErrors = firstParamError.getResolvableErrors();
            if (!resolvableErrors.isEmpty()) {
                errorMessage = resolvableErrors.iterator().next().getDefaultMessage();
        }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorBody(HttpStatus.BAD_REQUEST, "MethodValidationError",
                        errorMessage, request));
    }

    // Token-related exceptions
    @ExceptionHandler({MissingTokenException.class, InvalidTokenException.class, ExpiredTokenException.class, TokenException.class})
    public ResponseEntity<Map<String, Object>> handleTokenExceptions(TokenException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String type = ex.getClass().getSimpleName();
        return ResponseEntity.status(status)
                .body(buildErrorBody(status, type,
                        ex.getMessage() != null ? ex.getMessage() : "Authentication token issue.", request));
    }

    // JSON parse errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParse(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String errorMessage = "Invalid or missing field in request body.";
        String rootCauseMessage = ex.getMessage();
        
        // Get the root cause for more specific error detection
        Throwable rootCause = ex.getRootCause();
        String rootCauseMsg = rootCause != null ? rootCause.getMessage() : null;
        
        // Combine messages for better detection
        String allMessages = (rootCauseMessage != null ? rootCauseMessage : "") + 
                           (rootCauseMsg != null ? " " + rootCauseMsg : "");
        String lowerMessage = allMessages.toLowerCase();
        
        // Check for time format errors first (more specific, and "date" is substring of "localtime")
        if (lowerMessage.contains("localtime") || 
            (lowerMessage.contains("time") && !lowerMessage.contains("localdate") && 
             (lowerMessage.contains("could not be parsed") || lowerMessage.contains("text") || 
              lowerMessage.contains("parse") || lowerMessage.contains("format")))) {
            errorMessage = "Invalid time format. Use 'HH:mm' format (e.g., '21:00' or '09:00').";
        } 
        // Check for date format errors
        else if (lowerMessage.contains("localdate") || 
                 (lowerMessage.contains("date") && !lowerMessage.contains("localtime") &&
                  (lowerMessage.contains("could not be parsed") || lowerMessage.contains("text") || 
                   lowerMessage.contains("parse") || lowerMessage.contains("format")))) {
            // Check if this is for overtime or standby instructor endpoints
            String requestPath = request.getRequestURI() != null ? request.getRequestURI().toLowerCase() : "";
            if (requestPath.contains("overtime") || requestPath.contains("standby-instructor")) {
                errorMessage = "Invalid date format. Use 'dd-MM-yyyy' format (e.g., '21-01-2025').";
            } else {
                errorMessage = "Invalid date format. Use 'yyyy-MM-dd' format (e.g., '2025-11-01').";
            }
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorBody(HttpStatus.BAD_REQUEST, "JsonParseError",
                        errorMessage, request));
    }
}
