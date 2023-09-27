package org.example.dto;

import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record DateRangeDto(@PastOrPresent(message = "From date must be earlier than now") LocalDate from,
                           @PastOrPresent(message = "To date must be earlier than now") LocalDate to) {
    public boolean isRangeValid() {
        return from.isBefore(to);
    }
}
