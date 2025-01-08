package com.gateway.utils;

import org.springframework.validation.FieldError;

import java.util.List;

public record ErrorResponse(
        String status,
        int statusCode,
        String message,
        Object data,
        List<FieldError> fieldErrors
) {
}
