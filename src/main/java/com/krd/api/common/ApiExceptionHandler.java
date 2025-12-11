package com.krd.api.common;

import com.krd.starter.exception.ErrorResponse;
import com.krd.starter.user.exception.DuplicateUserException;
import com.krd.starter.user.exception.UserNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Domain-specific exception handler for this API.
 * Handles exceptions specific to the application's domain (users, etc.).
 *
 * <p>This handler has higher precedence than the global exception handler from
 * exception-handling-starter, allowing it to override handling for domain-specific exceptions.
 *
 * <p>Common exceptions (validation, authentication, authorization, etc.) are automatically
 * handled by the {@code GlobalExceptionHandler} from the exception-handling-starter.
 *
 * <p><b>Template Note:</b> This is a template showing how to handle domain-specific exceptions.
 * Add additional handlers here as your application grows and introduces new domain exceptions.
 *
 * @see com.krd.starter.exception.GlobalExceptionHandler
 * @see ErrorResponse
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // Higher precedence than starter's global handler
public class ApiExceptionHandler {

    /**
     * Handles user not found errors from the spring-api-starter.
     * Returns 404 Not Found.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(getRequestPath(request))
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    /**
     * Handles duplicate user errors from the spring-api-starter.
     * Returns 409 Conflict (more semantically correct than 400 for duplicates).
     */
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserException(
            DuplicateUserException ex,
            WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(getRequestPath(request))
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    /**
     * Extracts the request path from WebRequest for inclusion in error responses.
     */
    private String getRequestPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
