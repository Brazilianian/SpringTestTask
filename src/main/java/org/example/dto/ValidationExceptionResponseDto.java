package org.example.dto;

import java.util.Map;

public record ValidationExceptionResponseDto(String message,
                                             Map<String, String> errors) {
}
