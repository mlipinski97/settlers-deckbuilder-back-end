package pl.lipinski.settlers_deckbuilder.util.exception;

import org.springframework.http.HttpStatus;

public class PermissionDeniedException extends ControllerException{
    public PermissionDeniedException(String errorMessage, Integer errorCode) {
        super(errorMessage, errorCode, HttpStatus.FORBIDDEN);
    }
}
