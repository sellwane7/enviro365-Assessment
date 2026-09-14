package com.enviro.assessment.junior.sellwane.exception;

/**
 * Thrown when a withdrawal request breaks one of the business rules, e.g.:
 *  - retirement withdrawal but investor is not older than 65
 *  - amount exceeds balance
 *  - amount exceeds 90% of balance
 *
 * We use a dedicated exception type (rather than a generic RuntimeException)
 * so the GlobalExceptionHandler can map it specifically to a 400 Bad Request
 * with a clear message, instead of a scary 500 Internal Server Error.
 */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
