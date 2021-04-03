package pl.lipinski.settlers_deckbuilder.util.exception;

import org.springframework.http.HttpStatus;

public class WrongCredentialsException extends ControllerException{
    public WrongCredentialsException(String errorMessage, Integer errorCode) {
        super(errorMessage, errorCode, HttpStatus.NOT_FOUND);
    }
}
