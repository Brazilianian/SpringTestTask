package org.example.dto;

import java.time.LocalDate;

public record DateRange(LocalDate from,
                        LocalDate to) {
}
