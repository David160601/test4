package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class ApiException {
    final String message;
    final HttpStatus httpStatus;
    final ZonedDateTime zonedDateTime;
}
