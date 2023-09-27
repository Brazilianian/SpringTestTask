package org.example.exception.alreadyexists;

public abstract class AbstractAlreadyExistsException extends RuntimeException {
    public AbstractAlreadyExistsException(String message) {
        super(message);
    }
}
