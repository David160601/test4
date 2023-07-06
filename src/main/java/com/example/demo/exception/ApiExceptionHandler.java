package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = ApiRequestException.class)
    ResponseEntity<ApiException> apiRequestException(ApiRequestException apiRequestException) {
        ApiException apiException = new ApiException(apiRequestException.getMessage(), apiRequestException.getHttpStatus(), ZonedDateTime.now());
        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }
}
