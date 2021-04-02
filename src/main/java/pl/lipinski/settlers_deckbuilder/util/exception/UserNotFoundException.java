package pl.lipinski.settlers_deckbuilder.util.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ControllerException {
    public UserNotFoundException(String email, Integer errorCode) {
        super("User not found with given email: " + email, errorCode, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(Long userId, Integer errorCode) {
        super("User not found with given ID: " + userId, errorCode, HttpStatus.NOT_FOUND);
    }
}
