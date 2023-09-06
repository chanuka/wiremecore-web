package com.cba.core.wiremeweb.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidVariableValidator.class)
public @interface ValidVariable {
    String message() default "Variable cannot be null.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
