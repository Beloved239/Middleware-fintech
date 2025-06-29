package com.middleware.common.exception;

import org.springframework.http.HttpStatus;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

public class CustomerServiceException extends RuntimeException {
    private final HttpStatus httpStatus;

    public CustomerServiceException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CustomerServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CustomerServiceException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CustomerServiceException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    // Static factory methods for common exceptions
    public static CustomerServiceException notFound(String message) {
        return new CustomerServiceException(message, HttpStatus.NOT_FOUND);
    }

    public static CustomerServiceException conflict(String message) {
        return new CustomerServiceException(message, HttpStatus.CONFLICT);
    }

    public static CustomerServiceException unauthorized(String message) {
        return new CustomerServiceException(message, HttpStatus.UNAUTHORIZED);
    }

    public static CustomerServiceException forbidden(String message) {
        return new CustomerServiceException(message, HttpStatus.FORBIDDEN);
    }
}
