package org.example.domain;

import java.time.LocalDate;

public record DateRange(LocalDate from,
                        LocalDate to) {
}
