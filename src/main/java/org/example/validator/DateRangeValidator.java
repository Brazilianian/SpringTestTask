package org.example.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.annotation.ValidDateRange;
import org.example.domain.DateRange;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateRange> {
    @Override
    public boolean isValid(DateRange dateRange, ConstraintValidatorContext constraintValidatorContext) {
        return dateRange.from().isBefore(dateRange.to());
    }
}
