package com.cba.core.wiremeweb.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidVariableValidator implements ConstraintValidator<ValidVariable, String> {
    @Override
    public void initialize(ValidVariable constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && !value.isEmpty();
    }
}
