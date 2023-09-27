package org.example.dto.exception;

import java.util.Map;

public record ValidationExceptionResponseDto(String message,
                                             Map<String, String> errors) {
}
