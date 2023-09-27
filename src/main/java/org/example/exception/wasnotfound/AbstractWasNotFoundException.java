package org.example.exception.wasnotfound;

public abstract class AbstractWasNotFoundException extends RuntimeException {
    public AbstractWasNotFoundException(String message) {
        super(message);
    }
}
