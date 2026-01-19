package org.example.ecommercebackend.exception;

/**
 * Exception thrown when a request contains invalid data or parameters.
 * Results in HTTP 400 Bad Request response.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
