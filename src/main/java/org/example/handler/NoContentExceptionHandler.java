package org.example.handler;

import org.example.dto.exception.ExceptionResponseDto;
import org.example.exception.nocontent.AbstractNoContentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NoContentExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDto> catchAbstractNoContentException(AbstractNoContentException ex) {
        return new ResponseEntity<>(
                new ExceptionResponseDto(ex.getMessage()),
                HttpStatus.NO_CONTENT);
    }
}
