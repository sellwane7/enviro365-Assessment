package com.enviro.assessment.junior.sellwane.exception;

/**
 * Thrown when we look up an Investor or Product by ID and it doesn't exist.
 * Extends RuntimeException so we don't have to declare "throws" everywhere -
 * the GlobalExceptionHandler catches it and turns it into a 404 response.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
