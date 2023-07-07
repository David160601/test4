package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class IdValidator implements ConstraintValidator<IdValidation, Long> {
    public boolean isValid(Long id, ConstraintValidatorContext cxt) {
        if (id != null && id != 0) {
            return true;
        } else {
            return false;
        }

    }
}