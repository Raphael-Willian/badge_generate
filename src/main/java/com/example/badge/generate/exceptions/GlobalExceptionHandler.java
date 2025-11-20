package com.example.badge.generate.exceptions;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex) {
        ex.printStackTrace();
        ApiError error = new ApiError(ex.getMessage());
        return ResponseEntity.status(500).body(error);
    }
}

