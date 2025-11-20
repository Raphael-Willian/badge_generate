package com.example.badge.generate.exceptions;

import java.time.LocalDateTime;

public class ApiError {
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(String message) { this.message = message; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
