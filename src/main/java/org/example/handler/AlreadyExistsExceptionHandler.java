package org.example.handler;

import org.example.dto.exception.ExceptionResponseDto;
import org.example.dto.exception.ValidationExceptionResponseDto;
import org.example.exception.ValidationException;
import org.example.exception.alreadyexists.AbstractAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AlreadyExistsExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDto> catchAbstractAlreadyExistsException(AbstractAlreadyExistsException ex) {
        return new ResponseEntity<>(
                new ExceptionResponseDto(ex.getMessage()),
                HttpStatus.CONFLICT);
    }
}
