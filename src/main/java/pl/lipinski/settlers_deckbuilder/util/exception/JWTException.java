package pl.lipinski.settlers_deckbuilder.util.exception;

import org.springframework.http.HttpStatus;

public class JWTException extends ControllerException{
    public JWTException(Integer errorCode) {
        super("Error with JWT occurred", errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
