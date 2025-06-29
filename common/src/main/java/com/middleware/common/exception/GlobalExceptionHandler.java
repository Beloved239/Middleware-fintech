package com.middleware.common.exception;

import com.middleware.common.dto.MiddleWareResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle validation errors for request body (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MiddleWareResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        logger.warn("Validation failed for request: {}", request.getDescription(false));

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        MiddleWareResponse<Map<String, String>> response = MiddleWareResponse.<Map<String, String>>builder()
                .success(false)
                .message("Validation failed")
                .data(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle constraint violations (@Valid on method parameters)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MiddleWareResponse<Map<String, String>>> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        logger.warn("Constraint violation for request: {}", request.getDescription(false));

        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage()
                ));

        MiddleWareResponse<Map<String, String>> response = MiddleWareResponse.<Map<String, String>>builder()
                .success(false)
                .message("Validation failed")
                .data(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle database constraint violations (unique constraints, etc.)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<MiddleWareResponse<Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {

        logger.error("Data integrity violation for request: {}", request.getDescription(false), ex);

        String message = "Data integrity constraint violated";
        if (ex.getMessage().contains("Duplicate entry")) {
            message = "Duplicate entry - record already exists";
        } else if (ex.getMessage().contains("cannot be null")) {
            message = "Required field cannot be null";
        }

        MiddleWareResponse<Object> response = MiddleWareResponse.builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handle custom business exceptions
     */
    @ExceptionHandler(CustomerServiceException.class)
    public ResponseEntity<MiddleWareResponse<Object>> handleCustomerServiceException(
            CustomerServiceException ex, WebRequest request) {

        logger.error("Customer service exception for request: {}", request.getDescription(false), ex);

        MiddleWareResponse<Object> response = MiddleWareResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    /**
     * Handle method not supported
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MiddleWareResponse<Object>> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {

        logger.warn("Method not supported for request: {}", request.getDescription(false));

        MiddleWareResponse<Object> response = MiddleWareResponse.builder()
                .success(false)
                .message("HTTP method not supported: " + ex.getMethod())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    /**
     * Handle resource not found
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<MiddleWareResponse<Object>> handleNoHandlerFoundException(
            NoHandlerFoundException ex, WebRequest request) {

        logger.warn("No handler found for request: {}", request.getDescription(false));

        MiddleWareResponse<Object> response = MiddleWareResponse.builder()
                .success(false)
                .message("Resource not found: " + ex.getRequestURL())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handle illegal arguments
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MiddleWareResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        logger.warn("Illegal argument for request: {}", request.getDescription(false), ex);

        MiddleWareResponse<Object> response = MiddleWareResponse.builder()
                .success(false)
                .message("Invalid argument: " + ex.getMessage())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MiddleWareResponse<Object>> handleGenericException(
            Exception ex, WebRequest request) {

        logger.error("Unexpected error for request: {}", request.getDescription(false), ex);

        MiddleWareResponse<Object> response = MiddleWareResponse.builder()
                .success(false)
                .message("An unexpected error occurred. Please try again later.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler({ BadCredentialsException.class, AccessDeniedException.class})
    public ResponseEntity<Object> handleUnauthorizedException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDate.now());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Unauthorized");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
}