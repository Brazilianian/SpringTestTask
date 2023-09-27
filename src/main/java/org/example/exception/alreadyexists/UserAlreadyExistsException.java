package org.example.exception.alreadyexists;

public class UserAlreadyExistsException extends AbstractAlreadyExistsException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
