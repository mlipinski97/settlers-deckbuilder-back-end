package pl.lipinski.settlers_deckbuilder.util.exception;

import org.springframework.http.HttpStatus;

public class EmailTakenException extends ControllerException {
    public EmailTakenException(String errorMessage, Integer errorCode) {
        super(errorMessage, errorCode, HttpStatus.CONFLICT);
    }

    public EmailTakenException(Integer errorCode) {
        super("Given email address is already in use", errorCode, HttpStatus.CONFLICT);
    }
}
