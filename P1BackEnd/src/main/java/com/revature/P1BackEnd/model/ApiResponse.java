package com.revature.P1BackEnd.model;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

public record ApiResponse (
        String status,
        int statusCode,
        String message,
        Object data,
        List<FieldError> fieldErrors
) implements Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes for Redis  Caching
    public ApiResponse(String status, int code, String message, Object data) {
        this(status, code, message, data, null);
    }

    public ApiResponse(String message, Object data) {
        this("Success", HttpStatus.OK.value(), message, data, null);
    }


    public record FieldError(String field, String message) {
    }
}
