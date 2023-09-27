package org.example.dto;


import org.example.annotation.ValidDateRange;
import org.example.domain.DateRange;

public record BirthdayFilterDto(
        @ValidDateRange DateRange dateRange

        // Here also can be other variables for date filtering
) {
}
