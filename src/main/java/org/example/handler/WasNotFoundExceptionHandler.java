package org.example.handler;

import org.example.dto.ExceptionResponseDto;
import org.example.exception.wasnotfound.AbstractWasNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WasNotFoundExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDto> catchAbstractWasNotFoundException(AbstractWasNotFoundException ex) {
        return new ResponseEntity<>(
                new ExceptionResponseDto(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
