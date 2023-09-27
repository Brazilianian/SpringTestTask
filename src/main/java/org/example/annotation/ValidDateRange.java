package org.example.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.validator.DateRangeValidator;

import java.lang.annotation.*;


@Target({ElementType.FIELD,
        ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
@Documented
public @interface ValidDateRange {
    String message() default "Invalid range of date. From must be earlier than To";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
