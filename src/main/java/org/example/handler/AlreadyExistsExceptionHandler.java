package org.example.handler;

import org.example.dto.ValidationExceptionResponseDto;
import org.example.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AlreadyExistsExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ValidationExceptionResponseDto> catchValidationException(ValidationException ex) {
        return new ResponseEntity<>(
                new ValidationExceptionResponseDto(ex.getMessage(), ex.getErrors()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
