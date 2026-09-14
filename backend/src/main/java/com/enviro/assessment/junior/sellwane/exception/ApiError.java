package com.enviro.assessment.junior.sellwane.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A consistent JSON shape for every error the API returns, e.g.:
 * {
 *   "timestamp": "2026-09-10T10:15:30",
 *   "status": 400,
 *   "error": "Bad Request",
 *   "message": "Withdrawal amount exceeds available balance",
 *   "details": []
 * }
 * Having ONE consistent error shape makes it much easier for the frontend
 * to display error messages generically, instead of guessing the shape
 * every time.
 */
public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<String> details;

    public ApiError(int status, String error, String message, List<String> details) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }
}
