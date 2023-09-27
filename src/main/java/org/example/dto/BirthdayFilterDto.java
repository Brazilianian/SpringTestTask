package org.example.dto;


import org.example.annotation.ValidDateRange;

public record BirthdayFilterDto(
        @ValidDateRange DateRange dateRange
) {
}
