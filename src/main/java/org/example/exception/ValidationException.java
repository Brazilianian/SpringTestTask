package org.example.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = new HashMap<>();
    }

    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}
