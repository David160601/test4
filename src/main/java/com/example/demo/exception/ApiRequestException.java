package com.example.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ApiRequestException extends RuntimeException {
    final private HttpStatus httpStatus;
    public ApiRequestException(String message,HttpStatus httpStatus) {
        super(message);
        this.httpStatus=httpStatus;
    }
}
