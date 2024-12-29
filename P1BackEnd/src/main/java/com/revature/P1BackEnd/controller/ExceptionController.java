package com.revature.P1BackEnd.controller;


import com.revature.P1BackEnd.model.ApiResponse;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestControllerAdvice
public class ExceptionController {

    private final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("Exception occurred: {}, Request Details: {}",
                ex.getMessage(), request.getDescription(false), ex);
        return new ResponseEntity<>(
                new ApiResponse("Failed", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleNotFound(RuntimeException ex, WebRequest request) {
        logger.error("Exception occurred: {}, Request Details: {}",
                ex.getMessage(), request.getDescription(false), ex);
        return new ResponseEntity<>(
                new ApiResponse("Failed", HttpStatus.NOT_FOUND.value(), ex.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ApiResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiResponse.FieldError(error.getField(), error.getDefaultMessage()))
                .toList();

        return new ResponseEntity<>(new ApiResponse("Failed", HttpStatus.BAD_REQUEST.value(),
                "One or more fields have validation errors. Please check the details below.", null, fieldErrors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingParams(MissingServletRequestParameterException ex, WebRequest request) {
        logger.error("Parameter: {} is missing in the query parameters and is required., Request Details: {}",
                ex.getMessage(), request.getDescription(false), ex);
        return new ResponseEntity<>(
                new ApiResponse("Failed", HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(), "Parameter: " + ex.getParameterName() +
                        " is missing in the query parameters and is required."), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.error("Invalid argument: {}, Request Details: {}", ex.getMessage(), request.getDescription(false), ex);
        return new ResponseEntity<>(
                new ApiResponse("Failed", HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(BadRequestException ex, WebRequest request) {
        logger.error("Bad request: {}, Request Details: {}", ex.getMessage(), request.getDescription(false), ex);
        return new ResponseEntity<>(
                new ApiResponse("Failed", HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

}
